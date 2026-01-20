package com.myanime.infrastructure.configurations.feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;


@AllArgsConstructor
public class FeignConfig {
    @Bean
    @Profile("!dev")
    public Logger logger() {
        return new CoreFeignLogger();
    }

    @Bean
    public RequestInterceptor ndRequestInterceptor() {
        return template -> template.header("request_id", MDC.get("request_id"));
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new RetreiveMessageErrorDecoder();
    }
}