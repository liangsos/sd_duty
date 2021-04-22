package com.sd.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sd.modules.system.entity.Email;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface EmailMapper extends BaseMapper<Email> {

    @Select({"<script>","SELECT * FROM email WHERE 1=1 ", "<if test='begin != null'> AND sent_date &gt;=#{begin}</if>",
            "<if test='end != null'> AND sent_date &lt;=#{end}</if>",
            "</script>"})
    Page<Email> getEmailPage(Page<Email> page, Date begin, Date end);
}
