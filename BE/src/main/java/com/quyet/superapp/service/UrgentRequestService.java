package com.quyet.superapp.service;

import com.quyet.superapp.dto.UrgentRequestDTO;
import com.quyet.superapp.entity.UrgentRequest;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.BloodRequestStatus;
import com.quyet.superapp.mapper.UrgentRequestMapper;
import com.quyet.superapp.repository.UrgentRequestRepository;
import com.quyet.superapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrgentRequestService {
    private final UrgentRequestRepository urgentRepo;
    private final UserRepository userRepo;
    private final UrgentRequestMapper mapper;

    public UrgentRequestDTO create(UrgentRequestDTO dto) {
        // 1. Map DTO → Entity
        UrgentRequest entity = mapper.toEntity(dto);

        // 2. Gán thêm thông tin hệ thống
        entity.setRequestDate(LocalDate.now());
        User user = userRepo.findById(dto.getRequesterId())
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại: " + dto.getRequesterId()));
        entity.setRequester(user);

        // 3. Lưu
        UrgentRequest saved = urgentRepo.save(entity);

        // 4. Map Entity → DTO và trả về
        return mapper.toDto(saved);
    }

    public List<UrgentRequestDTO> getAll() {
        return urgentRepo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<UrgentRequestDTO> getByUser(Long userId) {
        return urgentRepo.findByRequesterUserId(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<UrgentRequestDTO> getByStatus(BloodRequestStatus status) {
        return urgentRepo.findByStatus(status)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public UrgentRequestDTO updateStatus(Long requestId, BloodRequestStatus status) {
        UrgentRequest request = urgentRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu: " + requestId));
        request.setStatus(status);
        return mapper.toDto(urgentRepo.save(request));
    }


}
