//package com.zhaopei.train.batch.job;
//
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//// /**
////  * 1、适合单体应用，不适合集群
////  * 2、没法实时更改定时任务状态和策略
////  */
//
//@Component
//@EnableScheduling
//public class SpringBootTestJob {
//
//    @Scheduled(cron = "0/5 * * * * ?")
//    private void test(){
//        //增加分布式锁，解决集群问题。即使如此也只能结局问题1，解决不了问题2。
//        System.out.println("SpringBootTestJob TEST");
//    }
//
//}
