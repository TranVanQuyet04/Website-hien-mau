package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodRequestDTO;
import com.quyet.superapp.dto.BloodRequestWithExistingPatientDTO;
import com.quyet.superapp.dto.CreateBloodRequestDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodRequestStatus;
import com.quyet.superapp.enums.UrgencyLevel;

import java.time.LocalDateTime;

public class BloodRequestMapper {

    // ✅ Entity → DTO
    public static BloodRequestDTO toDTO(BloodRequest req) {
        return BloodRequestDTO.builder()
                .bloodRequestId(req.getId())
                .requestCode(req.getRequestCode())
                .patientRecordCode(req.getPatientRecordCode())

                // 👤 Người gửi
                .requesterName(getSafeName(req.getRequester()))
                .requesterPhone(getSafePhone(req.getRequester()))

                // 👨‍⚕️ Bác sĩ phụ trách
                .doctorName(getSafeName(req.getDoctor()))
                .doctorPhone(getSafePhone(req.getDoctor()))

                // 🧑‍🦽 Bệnh nhân
                .patientName(req.getPatient() != null ? req.getPatient().getFullName() : null)
                .patientPhone(req.getPatient() != null ? req.getPatient().getPhone() : null)
                .patientAge(req.getPatient() != null ? req.getPatient().getAge() : null)
                .patientGender(req.getPatient() != null ? req.getPatient().getGender() : null)
                .patientWeight(req.getPatient() != null ? req.getPatient().getWeight() : null)
                .patientBloodGroup(req.getPatient() != null ? req.getPatient().getBloodGroup() : null)
                .suspectedPatientId(req.getPatient() != null ? req.getPatient().getId() : null)

                // 💉 Yêu cầu máu
                .bloodTypeName(req.getBloodType() != null ? req.getBloodType().getDescription() : null)
                .componentName(req.getComponent() != null ? req.getComponent().getName() : null)
                .quantityBag(req.getQuantityBag())
                .quantityMl(req.getQuantityMl())
                .urgencyLevel(req.getUrgencyLevel().name())
                .triageLevel(req.getTriageLevel())
                .reason(req.getReason())
                .neededAt(req.getNeededAt())

                // 🩸 Y sử
                .crossmatchRequired(req.getCrossmatchRequired())
                .hasTransfusionHistory(req.getHasTransfusionHistory())
                .hasReactionHistory(req.getHasReactionHistory())
                .isPregnant(req.getIsPregnant())
                .hasAntibodyIssue(req.getHasAntibodyIssue())

                // 📝 Ghi chú
                .warningNote(req.getWarningNote())
                .specialNote(req.getSpecialNote())

                // Trạng thái
                .status(req.getStatus() != null ? req.getStatus().name() : null)
                .confirmedVolumeMl(req.getConfirmedVolumeMl())
                .isUnmatched(req.getIsUnmatched())
                .codeRedId(req.getCodeRedId())
                .emergencyNote(req.getEmergencyNote())
                .approvedAt(req.getApprovedAt())
                .createdAt(req.getCreatedAt())

                .build();
    }

    // ✅ DTO → Entity
    // ✅ Overload cho CreateBloodRequestDTO
    public static BloodRequest toEntity(
            CreateBloodRequestDTO dto,
            User staff,
            User doctor,
            BloodType bloodType,
            BloodType expectedBloodType,
            BloodComponent component,
            String patientRecordCode,
            String requestCode,
            Patient patient
    ) {
        return BloodRequest.builder()
                .requester(staff)
                .doctor(doctor)
                .patient(patient)
                .bloodType(bloodType)
                .expectedBloodType(expectedBloodType)
                .component(component)
                .quantityBag(dto.getQuantityBag())
                .quantityMl(dto.getQuantityMl())
                .urgencyLevel(UrgencyLevel.valueOf(dto.getUrgencyLevel()))
                .triageLevel(dto.getTriageLevel())
                .reason(dto.getReason())
                .neededAt(dto.getNeededAt())
                .crossmatchRequired(dto.getCrossmatchRequired())
                .hasTransfusionHistory(dto.getHasTransfusionHistory())
                .hasReactionHistory(dto.getHasReactionHistory())
                .isPregnant(dto.getIsPregnant())
                .hasAntibodyIssue(dto.getHasAntibodyIssue())
                .warningNote(dto.getWarningNote())
                .specialNote(dto.getSpecialNote())
                .isUnmatched(dto.getIsUnmatched())
                .codeRedId(dto.getCodeRedId())
                .emergencyNote(dto.getEmergencyNote())
                .status(BloodRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .patientRecordCode(patientRecordCode)
                .requestCode(requestCode)
                .internalPriorityCode(dto.getInternalPriorityCode())
                .build();
    }




    // ✅ Helper
    private static String getSafeName(User user) {
        return (user != null && user.getUserProfile() != null)
                ? user.getUserProfile().getFullName()
                : null;
    }

    private static String getSafePhone(User user) {
        return (user != null && user.getUserProfile() != null)
                ? user.getUserProfile().getPhone()
                : null;
    }

    private static UrgencyLevel parseUrgencyLevel(String input) {
        if (input == null) return UrgencyLevel.BINH_THUONG;
        return switch (input.trim().toUpperCase()) {
            case "KHẨN CẤP" -> UrgencyLevel.KHAN_CAP;
            case "CẤP CỨU" -> UrgencyLevel.CAP_CUU;
            case "BÌNH THƯỜNG" -> UrgencyLevel.BINH_THUONG;
            default -> UrgencyLevel.valueOf(input.trim().toUpperCase().replace(" ", "_"));
        };
    }

}
