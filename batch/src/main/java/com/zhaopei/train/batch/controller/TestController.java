package com.zhaopei.train.batch.controller;


import com.zhaopei.train.batch.feign.BusinessFeign;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Resource
    BusinessFeign businessFeign;

    @GetMapping("/hello")
    public String hello(){
        String businessHello = businessFeign.hello();
        log.info(businessHello);
        return "Hello World Batch!";
    }
}
