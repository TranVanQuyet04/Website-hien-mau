package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BlogDTO;
import com.quyet.superapp.dto.BlogRequestDTO;
import com.quyet.superapp.dto.BlogResponseDTO;
import com.quyet.superapp.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<List<BlogResponseDTO>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> getBlogById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<BlogResponseDTO> createBlog(@RequestBody @Valid BlogRequestDTO blogDto) {
        return ResponseEntity.ok(blogService.save(blogDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
