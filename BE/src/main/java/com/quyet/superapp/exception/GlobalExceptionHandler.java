package com.quyet.superapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ 1. Lỗi validate @Valid (form nhập không đúng)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // ✅ 2. Lỗi nhập sai logic
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", ex.getMessage()
        ));
    }

    // ✅ 3. Lỗi người dùng (MemberException)
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Map<String, String>> handleMemberException(MemberException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "errorCode", ex.getErrorCode(),
                "message", ex.getMessage()
        ));
    }

    // ✅ 4. Lỗi nhập nhiều field cùng lúc (MultiFieldException)
    @ExceptionHandler(MultiFieldException.class)
    public ResponseEntity<Map<String, Object>> handleMultiFieldException(MultiFieldException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "errorCode", ex.getErrorCode(),
                "errors", ex.getFieldErrors()
        ));
    }

    // ✅ 5. Lỗi không tìm thấy dữ liệu
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "Không tìm thấy dữ liệu",
                "message", ex.getMessage()
        ));
    }

    // ✅ 6. Lỗi hệ thống (runtime lỗi bất ngờ)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Lỗi hệ thống",
                "message", ex.getMessage()
        ));
    }

    // ✅ 7. Fallback cho mọi exception chưa rõ
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Lỗi không xác định",
                "message", ex.getMessage()
        ));
    }

    // ✅ 8. Lỗi đăng ký tài khoản (hoặc thêm mới có check nhiều điều kiện trùng)
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, Object>> handleRegistrationException(RegistrationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", ex.getMessage());
        response.put("errors", ex.getErrors());
        return ResponseEntity.badRequest().body(response);
    }

}
