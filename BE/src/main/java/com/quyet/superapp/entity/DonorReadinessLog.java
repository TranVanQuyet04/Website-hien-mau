package com.quyet.superapp.entity;
import com.quyet.superapp.enums.DonorReadinessLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name = "DonorReadinessLog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class
DonorReadinessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_level", length = 30)
    private DonorReadinessLevel oldLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_level", length = 30)
    private DonorReadinessLevel newLevel;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}
