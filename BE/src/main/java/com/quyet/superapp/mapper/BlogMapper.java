package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BlogDTO;
import com.quyet.superapp.dto.BlogRequestDTO;
import com.quyet.superapp.dto.BlogResponseDTO;
import com.quyet.superapp.entity.Blog;
import com.quyet.superapp.enums.BlogStatus;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper {

    public Blog toEntity(BlogRequestDTO dto) {
        Blog blog = new Blog();
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setStatus(BlogStatus.valueOf(dto.getStatus()));
        return blog;
    }

    public BlogResponseDTO toResponseDto(Blog blog) {
        BlogResponseDTO dto = new BlogResponseDTO();
        dto.setBlogId(blog.getBlogId());
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setPreviewContent(generatePreview(blog.getContent()));
        dto.setStatus(blog.getStatus());
        dto.setCreatedAt(blog.getCreatedAt());
        dto.setUpdatedAt(blog.getUpdatedAt());
        dto.setAuthorName(blog.getAuthor().getUserProfile().getFullName());
        dto.setAuthorId(blog.getAuthor().getUserId());
        dto.setThumbnailUrl(blog.getThumbnailUrl());
        dto.setTags(blog.getTags());
        dto.setViewCount(blog.getViewCount());
        return dto;
    }

    private String generatePreview(String content) {
        return content != null && content.length() > 100
                ? content.substring(0, 100) + "..."
                : content;
    }
}
