package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BlogStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogResponseDTO {
    private Long blogId;
    private String title;
    private String content;
    private String previewContent;
    private BlogStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String authorName;
    private Long authorId;
    private String thumbnailUrl;
    private List<String> tags;
    private Integer viewCount;
}

