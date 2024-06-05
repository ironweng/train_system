package com.zhaopei.train.member.config;

import com.zhaopei.train.common.interceptor.LogInterceptor;
import com.zhaopei.train.common.interceptor.MemberInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

   @Autowired
   MemberInterceptor memberInterceptor;

   @Autowired
   LogInterceptor logInterceptor;

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
       //这是打印日志的拦截器
       registry.addInterceptor(logInterceptor);
       // 路径不要包含context-path
       registry.addInterceptor(memberInterceptor)
               .addPathPatterns("/**")
               .excludePathPatterns(
                       "/hello",
                       "/member/send-code",
                       "/member/login"
               );
   }
}
