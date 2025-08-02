package com.quyet.superapp.listener;

import com.quyet.superapp.event.EmailNotificationEvent;
import com.quyet.superapp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationListener {
    private final EmailService emailService;

    @EventListener
    public void handleEmailEvent(EmailNotificationEvent event) {
        emailService.sendEmail(
                event.getUser(),
                event.getSubject(),
                event.getContent(),
                event.getType()
        );
    }
}
