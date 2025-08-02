package com.quyet.superapp.email;

/**
 * Interface dùng để sinh tiêu đề và nội dung email
 * tuỳ theo EmailType và dữ liệu truyền vào.
 */
public interface EmailTemplateBuilder {
    String buildSubject(com.quyet.superapp.enums.EmailType type, Object data);
    String buildBody(com.quyet.superapp.enums.EmailType type, Object data);
}
