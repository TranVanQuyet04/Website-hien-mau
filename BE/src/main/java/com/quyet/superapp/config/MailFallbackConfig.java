package com.quyet.superapp.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Render/prod can disable Mail auto-config via spring.autoconfigure.exclude.
 * This fallback prevents startup failure when code autowires JavaMailSender.
 * If mail properties are not provided, sending will fail gracefully at runtime.
 */
@Configuration
public class MailFallbackConfig {

    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
}

