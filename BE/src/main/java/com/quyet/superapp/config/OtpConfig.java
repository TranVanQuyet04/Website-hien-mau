package com.quyet.superapp.config;

import com.quyet.superapp.service.InMemoryOtpService;
import com.quyet.superapp.service.OtpService;
import com.quyet.superapp.service.RedisOtpService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtpConfig {

    @Bean
    public OtpService otpService(ObjectProvider<RedisOtpService> redisOtpService) {
        RedisOtpService redis = redisOtpService.getIfAvailable();
        return (redis != null) ? redis : new InMemoryOtpService();
    }
}

