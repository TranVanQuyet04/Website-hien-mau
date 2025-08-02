package com.quyet.superapp.event;

import com.quyet.superapp.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailNotificationEvent extends ApplicationEvent {
    private final User user;
    private final String subject;
    private final String content;
    private final String type;
    public EmailNotificationEvent(Object source, User user, String subject, String content, String type) {
        super(source);
        this.user = user;
        this.subject = subject;
        this.content = content;
        this.type = type;
    }
}
