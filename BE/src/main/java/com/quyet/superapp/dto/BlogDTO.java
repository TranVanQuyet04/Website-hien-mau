package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {
    private Long blogId;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private Long authorId;
}
