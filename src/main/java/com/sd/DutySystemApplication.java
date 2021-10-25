package com.sd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication
@MapperScan({"com.sd.mapper","com.sd.modules.system.mapper"})
// @EnableScheduling
public class DutySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DutySystemApplication.class, args);
    }

}
