package com.sd.modules.system.service.impl;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sd.modules.system.entity.Email;
import com.sd.modules.system.entity.SenderWhiteList;
import com.sd.modules.system.mapper.EmailMapper;
import com.sd.modules.system.mapper.SenderWhiteListMapper;
import com.sd.modules.system.service.EmailService;
import com.sd.pojo.EmailInfo;
import com.sd.pojo.EmailServerInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${email.file-path}")
    private String emailFilePath;
    @Value("${email.host}")
    private String emailHost;
    @Autowired
    private EmailServerInfo emailServerInfo;
    @Autowired
    private SenderWhiteListMapper senderWhiteListMapper;
    @Autowired
    private EmailMapper emailMapper;

    @Override
    public IPage<Email> getEmails(long current, long size, String beginTime, String endTime) {
//        readEmailSecheduled();
        Date begin = null;
        Date end = null;
        if (StrUtil.isNotBlank(beginTime)) {
            begin = DateUtil.parse(beginTime);
        }
        if (StrUtil.isNotBlank(endTime)) {
            end = DateUtil.parse(endTime);
        }
        Page<Email> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn("sent_date").setAsc(false);

        page.addOrder(orderItem);
        page = emailMapper.getEmailPage(page, begin, end);
        // page = emailMapper.selectPage(page, null);
        List<Email> list = page.getRecords();
        for (Email email : list) {
            String file = email.getFiles();
            if (StrUtil.isNotBlank(file)) {
                List<String> files = Arrays.asList(file.split("\\|"));
                List<String> fileList = files.stream().map(f -> emailHost + f).collect(Collectors.toList());
                email.setFiles(String.join("|", fileList));
            }
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyEmail(Long id, String content) {
        Email email = emailMapper.selectById(id);
        String msgId = email.getMsgId();
        List<EmailInfo> emailInfos = getAllEmailInfos();
        for (int i = emailInfos.size() - 1; i >= 0; i--) {
            EmailInfo emailInfo = emailInfos.get(i);
            if (emailInfo.getMessageID().equals(msgId)) {
                int msgnum = i + 1;
                replyEmail(msgnum, content, null);
                email.setUpdateTime(new Date());
                email.setReply(content);
                email.setStatus(2);
                emailMapper.updateById(email);
                break;
            }
        }
    }

    /**
     * 回复第 msgnum 份邮件
     * 
     * @param emailServerInfo
     * @param msgnum
     * @param replyEmail
     * @return
     */
    private boolean replyEmail(int msgnum, String content, String[] attachmentFiles) {

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.pop3s.host", emailServerInfo.getMailServerPOP3Host());
        properties.put("mail.pop3s.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", emailServerInfo.getMailServerSMTPHost());
        properties.put("mail.smtp.port", "25");
        Session session = Session.getDefaultInstance(properties);

        Store store = null;
        Folder folder = null;
        try {
            // Get a Store object and connect to the current host
            store = session.getStore("pop3s");
            store.connect(emailServerInfo.getMailServerPOP3Host(), emailServerInfo.getMyEmailAddress(),
                    emailServerInfo.getPassword());// change the user and
                                                   // password accordingly

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message message = folder.getMessage(msgnum);
            String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));

            Message replyMessage = new MimeMessage(session);
            replyMessage = (MimeMessage) message.reply(false);
            replyMessage.setFrom(new InternetAddress(to));

            // 设置回复的邮件地址
            replyMessage.setReplyTo(message.getReplyTo());

            // Multipart is a container that holds multiple body parts.
            Multipart bodyPartContainer = new MimeMultipart();
            BodyPart bodyPart = new MimeBodyPart();
            // 设置回复的正文
            bodyPart.setContent(content, "text/html; charset=UTF-8");
            bodyPartContainer.addBodyPart(bodyPart);

            if (attachmentFiles != null && attachmentFiles.length > 0) { // 存在附件
                for (String fileName : attachmentFiles) { // 遍历所有的附件
                    bodyPart = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileName); // 得到数据源
                    bodyPart.setDataHandler(new DataHandler(fds)); // 得到附件本身并至入BodyPart
                    bodyPart.setFileName(fds.getName()); // 得到文件名同样至入BodyPart
                    bodyPartContainer.addBodyPart(bodyPart);
                }
            }
            // 设置邮件消息的主要内容
            replyMessage.setContent(bodyPartContainer); // Multipart加入到信件

            // Send the message by authenticating the SMTP server
            // Create a Transport instance and call the sendMessage
            Transport t = session.getTransport("smtp");
            try {
                // connect to the smpt server using transport instance
                t.connect(emailServerInfo.getMailServerSMTPHost(), emailServerInfo.getMyEmailAddress(),
                        emailServerInfo.getPassword());
                t.sendMessage(replyMessage, replyMessage.getAllRecipients());
                return true;
            } finally {
                t.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // close the store and folder objects
            if (folder != null) {
                try {
                    folder.close(false);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 读取邮件定时任务
     */
     @Scheduled(initialDelay = 5 * 1000, fixedDelay = 60 * 60 * 1000)
    public void readEmailSecheduled() {
        List<EmailInfo> emailInfos = getAllEmailInfos();
        Date now = new Date();
        if (emailInfos == null) {
            log.error("-----{}-----获取邮件失败-----", now);
            return;
        }
        log.warn("-----{}-----获取邮件成功-----{}封-----", now, emailInfos.size());
//        emailInfos = emailInfos.stream().filter(e -> DateUtil.between(now, e.getSentDate(), DateUnit.HOUR) <= 3)
//                .collect(Collectors.toList());
        if (!emailInfos.isEmpty()) {

            List<SenderWhiteList> whiteList = senderWhiteListMapper.selectList(null);
            Set<String> whiteEmails = whiteList.stream().map(SenderWhiteList::getEmail).collect(Collectors.toSet());
            for (EmailInfo emailInfo : emailInfos) {
                String fromAddress = emailInfo.getFromAddress();
                Date sentDate = emailInfo.getSentDate();
                if (whiteEmails.stream().anyMatch(we -> fromAddress.contains(we))) {
                    String msgId = emailInfo.getMessageID();
                    QueryWrapper<Email> emailQw = new QueryWrapper<>();
                    emailQw.eq("msg_id", msgId);
                    if (emailMapper.selectCount(emailQw) > 0) {
                        continue;
                    }
                    Email email = new Email();
                    email.setSubject(emailInfo.getSubject());
                    email.setContent(emailInfo.getContent());
                    List<String> attachmentFiles = emailInfo.getAttachmentFiles().stream()
                            .map(af -> af.replace(emailFilePath, "")).collect(Collectors.toList());
                    email.setFiles(String.join("|", attachmentFiles));
                    email.setFromAddress(fromAddress);
                    email.setSentDate(sentDate);
                    email.setMsgId(msgId);
                    email.setStatus(0);
                    email.setCreateTime(now);
                    email.setUpdateTime(now);
                    emailMapper.insert(email);
                }
            }
        }

    }

    /**
     * 获取所有邮件
     * 
     * @param emailServerInfo
     * @return
     */
    private List<EmailInfo> getAllEmailInfos() {
        // 如果登陆成功，则进行发送邮件

        Session sendMailSession = loginEmailServer(true);
        if (sendMailSession != null) {
            // sendMailSession.setDebug(true);
            List<EmailInfo> emailInfos = readAllEmailInfos(sendMailSession);
            return emailInfos;
        } else {
            return null;
        }
    }

    /**
     * 读取所有邮件
     * 
     * @param sendMailSession
     * @param emailServerInfo
     * @return
     */
    private List<EmailInfo> readAllEmailInfos(Session sendMailSession) {

        List<EmailInfo> allEmailInfos = null;
        Store store = null;
        try {
            store = sendMailSession.getStore("pop3");
            store.connect(emailServerInfo.getUserName(), emailServerInfo.getPassword());

            allEmailInfos = fetchingAllEmailInfos(store, true);

            // close the store
            return allEmailInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private List<EmailInfo> fetchingAllEmailInfos(Store store, boolean closeFolder) throws Exception {
        List<EmailInfo> emailInfos = new ArrayList<>();

        // create the folder object and open it
        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        // retrieve all messages from the folder in an array
        Message[] messages = emailFolder.getMessages();
        for (Message message : messages) {
            EmailInfo emailInfo = new EmailInfo();
            writePart(message, emailInfo);
            emailInfos.add(emailInfo);
        }
        if (closeFolder) {
            emailFolder.close(false);
        }
        return emailInfos;
    }

    /*
     * This method checks for content-type based on which, it processes and fetches
     * the content of the message
     */
    private void writePart(Part p, EmailInfo emailInfo) throws Exception {
        if (p instanceof Message)
            // Call methos writeEnvelope
            writeEnvelope((Message) p, emailInfo);

        // check if the content is plain text
        if (p.isMimeType("text/plain")) {
            // 设置文本内容的正文
            emailInfo.setContent(MimeUtility.decodeText(p.getContent().toString()));
        }
        // check if the content has attachment
        else if (p.isMimeType("multipart/*")) {
            emailInfo.setContainsAttachments(true);
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                writePart(mp.getBodyPart(i), emailInfo);
        }
        // check if the content is a nested message // 包含内嵌的内容
        else if (p.isMimeType("message/rfc822")) {
            writePart((Part) p.getContent(), emailInfo);
        }
        // check if the content is an inline image
        else if (p.isMimeType("image/jpeg")) { // emailInfo
            Object o = p.getContent();
            InputStream x = (InputStream) o;
            // Construct the required byte array

            // 开启线程保存文件
            new SaveFileThread(x, "image.jpg").start();

        } else if (p.getContentType().contains("image/")) {
            File f = new File(emailFilePath + "image/" + new Date().getTime() + ".jpg");
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
            com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p.getContent();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = test.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
            output.close();
        } else {
            Object o = p.getContent();
            if (o instanceof String) {
                // 设置文本内容的正文
                emailInfo.setContent(MimeUtility.decodeText(p.getContent().toString()));
            } else if (o instanceof InputStream) {

                String attachmentFileName = p.getDataHandler().getDataSource().getName();
                if (attachmentFileName != null) {
                    attachmentFileName = MimeUtility.decodeText(attachmentFileName);
                    attachmentFileName = DateUtil.format(emailInfo.getSentDate(), "yy/MMdd/HHmmss/")
                            + attachmentFileName;
                    InputStream fileIn = p.getDataHandler().getDataSource().getInputStream();
                    List<String> attachmentFiles = emailInfo.getAttachmentFiles();
                    attachmentFiles.add(emailFilePath + attachmentFileName);

                    // 保存附件路径及名称
                    emailInfo.setAttachmentFiles(attachmentFiles);
                    // 开启线程保存文件
                    new SaveFileThread(fileIn, attachmentFileName).start();
                }

            } else {
                // log.info("This is an unknown type");
                // log.info("---------------------------");
                // log.info(o.toString());
            }
        }

    }

    /*
     * This method would print FROM,TO and SUBJECT of the message
     */
    private static void writeEnvelope(Message m, EmailInfo emailInfo) throws Exception {
        Address[] a;

        // 设置发送时间
        emailInfo.setSentDate(m.getSentDate());
        // FROM
        if ((a = m.getFrom()) != null) {
            // 注意需要 decode
            emailInfo.setFromAddress(MimeUtility.decodeText(a[0].toString()));
        }

        // TO
        try {
            a = m.getRecipients(Message.RecipientType.TO);
        } catch (AddressException e) {
            e.printStackTrace();
        }
        if (a != null) {
            String[] toes = new String[a.length];
            for (int j = 0; j < a.length; j++) {
                // log.info("TO address: " + MimeUtility.decodeText(a[j].toString()));
                toes[j] = MimeUtility.decodeText(a[j].toString());
            }
            emailInfo.setToAddress(toes);
        }

        // CC
        try {
            a = m.getRecipients(Message.RecipientType.CC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (a != null) {
            String[] toes = new String[a.length];
            for (int j = 0; j < a.length; j++) {
                // log.info("TO CC: " + MimeUtility.decodeText(a[j].toString()));
                toes[j] = MimeUtility.decodeText(a[j].toString());
            }
            emailInfo.setCarbonCopy(toes);
        }

        // BCC
        try {
            a = m.getRecipients(Message.RecipientType.BCC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (a != null) {
            String[] toes = new String[a.length];
            for (int j = 0; j < a.length; j++) {
                toes[j] = MimeUtility.decodeText(a[j].toString());
            }
            emailInfo.setDarkCopy(toes);
        }

        // SUBJECT
        if (m.getSubject() != null) {
            emailInfo.setSubject(MimeUtility.decodeText(m.getSubject()));
        }

        // 判断邮件是否已读
        boolean isNew = false;
        Flags flags = m.getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isNew = true;
                // break;
            }
        }
        emailInfo.setReaded(isNew);
        /*
         * This message is seen. This flag is implicitly set by the implementation when
         * the this Message's content is returned to the client in some form. The
         * getInputStream and getContent methods on Message cause this flag to be set.
         */
        // emailInfo.setReaded(false);

        // 判断是否需要回执
        boolean needReply = m.getHeader("Disposition-Notification-To") != null ? true : false;
        emailInfo.setNeedReply(needReply);

        // 获取该邮件的Message-ID
        String messageID = ((MimeMessage) m).getMessageID();
        emailInfo.setMessageID(messageID);
    }

    /**
     * 保存附件的线程
     * 
     * @author dell
     *
     */
    private class SaveFileThread extends Thread {

        private String filename;
        private InputStream fileIn;

        public SaveFileThread(InputStream fileIn, String filename) {
            this.filename = filename;
            this.fileIn = fileIn;
        }

        @Override
        public void run() {
            FileOutputStream out = null;
            String filePath = emailFilePath + filename;
            try {
                if (new File(filePath).exists()) {
                    return;
                }
                File dir = new File(filePath.substring(0, filePath.lastIndexOf("/")));
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                out = new FileOutputStream(filePath);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = fileIn.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        out = null;
                    }
                    if (fileIn != null) {
                        fileIn.close();
                        fileIn = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据 EmailServerInfo 信息登陆邮件服务器，返回mail回话对象
     * 
     * @param emailServerInfo
     * @return
     */
    private Session loginEmailServer(boolean useReadProtocol) {
        Session sendMailSession = null;
        Authenticator authentication = null;

        try {
            Properties properties = getProperties(emailServerInfo, useReadProtocol);
            // 如果需要身份认证，则创建一个密码验证器
            if (emailServerInfo.isValidate()) {
                authentication = new Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailServerInfo.getUserName(), emailServerInfo.getPassword());
                    }
                };
            }
            // 获取回话对象
            sendMailSession = Session.getDefaultInstance(properties, useReadProtocol ? null : authentication);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sendMailSession;
    }

    /**
     * 获得邮件会话属性 <b>注：此处需要适配 SMTP、POP3、IMAP</>
     * 
     */
    private Properties getProperties(EmailServerInfo emailServerInfo, boolean useReadProtocol) {
        Properties p = new Properties();
        if (useReadProtocol) {
            p.put("mail.pop3.host", emailServerInfo.getMailServerPOP3Host());
            p.put("mail.pop3.port", "110");
            p.put("mail.pop3.auth", emailServerInfo.isValidate() ? "true" : "false");
            p.put("mail.pop3s.starttls.enable", "true");
        } else {
            p.put("mail.smtp.host", emailServerInfo.getMailServerSMTPHost());
            p.put("mail.smtp.port", "25");
            p.put("mail.smtp.auth", emailServerInfo.isValidate() ? "true" : "false");
            p.put("mail.smtp.starttls.enable", "true");
        }
        return p;
    }

}
