package com.zhaopei.train.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class Test1Filter implements GlobalFilter , Ordered {
    //多个过滤器的时候,可以实现Ordered接口,这样可以定义多个过滤器的执行顺序
    //getOrder()方法返回的int值越小,越先执行

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Test1Filter");
        //这里可以理解为这个过滤器走完，接着走下一个过滤器
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
