package com.quyet.superapp.service;

import com.quyet.superapp.dto.RequestLogDTO;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.RequestLog;
import com.quyet.superapp.mapper.RequestLogMapper;
import com.quyet.superapp.repository.BloodRequestRepository;
import com.quyet.superapp.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RequestLogService {

    private final RequestLogRepository requestLogRepo;
    private final BloodRequestRepository bloodRequestRepo;
    private final RequestLogRepository requestLogRepository;
    private final RequestLogMapper logMapper;


    List<RequestLogDTO> getLogsByRequestId(Long requestId) {
        BloodRequest request = bloodRequestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn truyền máu"));

        return requestLogRepository.findByBloodRequestOrderByTimestampAsc(request)
                .stream()
                .map(logMapper::toDTO)  // nếu có DTO
                .collect(Collectors.toList());
    }


    /**
     * Ghi log hành động xử lý đơn yêu cầu máu.
     * @param request Đơn truyền máu liên quan
     * @param action  Hành động (VD: STAFF_CREATED, ADMIN_APPROVED, PAID, CONFIRMED_RECEIVED)
     * @param performedBy Người thực hiện (username hoặc userId)
     * @param note    Ghi chú thêm nếu có
     */
    public void logRequestAction(BloodRequest request, String action, String performedBy, String note) {
        RequestLog log = new RequestLog();
        log.setBloodRequest(request);
        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setTimestamp(LocalDateTime.now());
        log.setNote(note);
        requestLogRepo.save(log);
    }
}
