package com.zhaopei.train.member.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
@ComponentScan("com.zhaopei")
@MapperScan("com.zhaopei.train.*.mapper")
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication app=new SpringApplication(MemberApplication.class);
        Environment env=app.run(args).getEnvironment();
        log.info("启动成功!");
        log.info("测试地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }

}
