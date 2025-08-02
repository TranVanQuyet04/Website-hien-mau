package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

    /**
     * Lấy toàn bộ lịch sử xử lý theo đơn truyền máu, sắp xếp theo thời gian tăng dần.
     */
    List<RequestLog> findByBloodRequestOrderByTimestampAsc(BloodRequest request);
}
