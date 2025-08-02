    package com.quyet.superapp.service;
    
    import com.quyet.superapp.dto.BlogDTO;
    import com.quyet.superapp.dto.BlogRequestDTO;
    import com.quyet.superapp.dto.BlogResponseDTO;
    import com.quyet.superapp.entity.Blog;
    import com.quyet.superapp.entity.User;
    import com.quyet.superapp.mapper.BlogMapper;
    import com.quyet.superapp.repository.BlogRepository;
    import com.quyet.superapp.repository.UserRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    
    import java.time.LocalDateTime;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class BlogService {

        private final BlogRepository blogRepository;
        private final UserRepository userRepository;
        private final BlogMapper blogMapper;

        public BlogResponseDTO getById(Long id) {
            Blog blog = blogRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài viết với ID: " + id));
            return blogMapper.toResponseDto(blog);
        }

        public List<BlogResponseDTO> getAll() {
            return blogRepository.findAll().stream()
                    .map(blogMapper::toResponseDto)
                    .toList();
        }

        public BlogResponseDTO save(BlogRequestDTO dto) {
            Blog blog = blogMapper.toEntity(dto);

            User author = userRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tác giả với ID: " + dto.getAuthorId()));

            blog.setAuthor(author);
            blog.setCreatedAt(LocalDateTime.now());

            Blog saved = blogRepository.save(blog);
            return blogMapper.toResponseDto(saved);
        }

        public void deleteById(Long id) {
            if (!blogRepository.existsById(id)) {
                throw new IllegalArgumentException("Không tìm thấy blog để xóa với ID: " + id);
            }
            blogRepository.deleteById(id);
        }

        public long countActiveBlogs() {
            return blogRepository.countByStatus("PUBLISHED"); // Nếu dùng enum
        }
    }
