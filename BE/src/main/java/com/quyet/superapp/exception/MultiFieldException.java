package com.quyet.superapp.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class MultiFieldException extends RuntimeException {
    private final String errorCode; // Ví dụ: "Đăng ký tài khoản thất bại"
    private final Map<String, String> fieldErrors; // Ví dụ: {"email": "Email đã tồn tại"}

    public MultiFieldException(String errorCode, Map<String, String> fieldErrors) {
        super("Nhiều lỗi xảy ra khi tạo tài khoản");
        this.errorCode = errorCode;
        this.fieldErrors = fieldErrors;
    }
}
