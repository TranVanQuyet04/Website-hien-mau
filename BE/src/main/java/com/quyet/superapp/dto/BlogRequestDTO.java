package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogRequestDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String status;

    @NotNull
    private Long authorId;

}
