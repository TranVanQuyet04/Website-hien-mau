package com.quyet.superapp.repository;

import com.quyet.superapp.dto.BloodInventoryAlertDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.RoleEnum;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.scheduler.InventoryAlertScheduler;
import com.quyet.superapp.service.BloodService;
import com.quyet.superapp.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryAlertSchedulerTest {

    @Mock
    private BloodService bloodService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private InventoryAlertScheduler scheduler;

    @Test
    public void testAutoCheckInventoryAlert_whenCriticalExists_thenSendNotification() {
        // Mock dữ liệu cảnh báo
        BloodInventoryAlertDTO criticalAlert = new BloodInventoryAlertDTO(
                1L, "O+", "Hồng cầu", 150, 300, 200, "CRITICAL"
        );

        User admin = new User(1L); // Chỉ cần ID là đủ

        when(bloodService.getAllInventoryStatus()).thenReturn(List.of(criticalAlert));
        when(userRepository.findFirstByRole(RoleEnum.ADMIN)).thenReturn(Optional.of(admin));

        // Gọi hàm cần test
        scheduler.autoCheckInventoryAlert();

        // Kiểm tra đã gửi thông báo
        verify(notificationService, times(1)).createNotification(
                eq(admin.getUserId()),
                contains("Cảnh báo kho máu"),
                contains("O+")
        );
    }

    @Test
    public void testAutoCheckInventoryAlert_whenNoCritical_thenDoNothing() {
        when(bloodService.getAllInventoryStatus()).thenReturn(List.of());

        scheduler.autoCheckInventoryAlert();

        verifyNoInteractions(notificationService);
    }
}
