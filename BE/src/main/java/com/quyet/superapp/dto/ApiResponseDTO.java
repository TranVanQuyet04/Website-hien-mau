package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private T data;
    private String message;

    public ApiResponseDTO(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    // ✅ Factory methods (giúp Controller viết gọn gàng)
    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return new ApiResponseDTO<>(true, data, message);
    }

    public static <T> ApiResponseDTO<T> success(String message) {
        return new ApiResponseDTO<>(true, null, message);
    }

    public static <T> ApiResponseDTO<T> fail(String message) {
        return new ApiResponseDTO<>(false, null, message);
    }
}
