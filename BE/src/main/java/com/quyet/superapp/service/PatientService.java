package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodRequestWithNewPatientDTO;
import com.quyet.superapp.dto.CreateBloodRequestDTO;
import com.quyet.superapp.entity.Patient;
import com.quyet.superapp.exception.RegistrationException;
import com.quyet.superapp.mapper.PatientMapper;
import com.quyet.superapp.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepo;
    private final PatientMapper patientMapper;

    // ✅ Dùng cho DTO dạng đầy đủ (khi biết CCCD)
    public Patient getOrCreateFromDTO(CreateBloodRequestDTO dto) {
        validateInsurance(dto);
        if (dto.getCitizenId() != null) {
            return patientRepo.findByCitizenId(dto.getCitizenId())
                    .orElseGet(() -> patientRepo.save(patientMapper.toEntity(dto)));
        }
        return patientRepo.save(patientMapper.toEntity(dto));
    }

    // ✅ Dùng khi tạo mới từ yêu cầu chưa biết CCCD
    public Patient getOrCreateFromDTO(BloodRequestWithNewPatientDTO dto) {
        validateInsurance(dto);
        if (dto.getCitizenId() != null) {
            return patientRepo.findByCitizenId(dto.getCitizenId())
                    .orElseGet(() -> patientRepo.save(patientMapper.toEntity(dto)));
        }
        return patientRepo.save(patientMapper.toEntity(dto));
    }

    // ✅ Tìm theo ID nếu đã có từ nghi ngờ (suspectedPatientId)
    public Patient findById(Long id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với ID = " + id));
    }

    // ✅ Validate BHYT: nếu có thì phải có ngày và không trùng
    /**
     * Kiểm tra thông tin bảo hiểm y tế từ DTO nhập vào.
     * <p>
     * - Nếu có chọn "có bảo hiểm" thì phải có:
     *   + Số thẻ BHYT (không trống, không trùng)
     *   + Ngày hết hạn BHYT (không null)
     *
     * @param dto Dữ liệu yêu cầu truyền máu với bệnh nhân mới
     * @throws RegistrationException nếu thông tin BHYT không hợp lệ
     */
    public void validateInsurance(BloodRequestWithNewPatientDTO dto) {
        Map<String, String> errors = new HashMap<>();

        if (Boolean.TRUE.equals(dto.getHasInsurance())) {
            String cardNumber = dto.getInsuranceCardNumber();
            if (cardNumber == null || cardNumber.isBlank()) {
                errors.put("insuranceCardNumber", "Vui lòng nhập số thẻ BHYT");
            } else if (patientRepo.existsByInsuranceCardNumber(cardNumber)) {
                errors.put("insuranceCardNumber", "Số thẻ BHYT đã tồn tại");
            }

            if (dto.getInsuranceValidTo() == null) {
                errors.put("insuranceValidTo", "Vui lòng nhập ngày hết hạn BHYT");
            }
        }

        if (!errors.isEmpty()) {
            throw new RegistrationException("Thông tin BHYT không hợp lệ", errors);
        }
    }


    // ✅ Overload cho CreateBloodRequestDTO (nếu bạn cũng dùng BHYT trong đó)
    private void validateInsurance(CreateBloodRequestDTO dto) {
        Map<String, String> errors = new HashMap<>();

        if (Boolean.TRUE.equals(dto.getHasInsurance())) {
            if (dto.getInsuranceCardNumber() == null || dto.getInsuranceCardNumber().isBlank()) {
                errors.put("insuranceCardNumber", "Vui lòng nhập số thẻ BHYT");
            } else if (patientRepo.existsByInsuranceCardNumber(dto.getInsuranceCardNumber())) {
                errors.put("insuranceCardNumber", "Số thẻ BHYT đã tồn tại");
            }

            if (dto.getInsuranceValidTo() == null) {
                errors.put("insuranceValidTo", "Vui lòng nhập ngày hết hạn BHYT");
            }
        }

        if (!errors.isEmpty()) {
            throw new RegistrationException("Thông tin BHYT không hợp lệ", errors);
        }
    }
}



//package com.quyet.superapp.service;
//
//import com.quyet.superapp.dto.BloodRequestWithNewPatientDTO;
//import com.quyet.superapp.dto.CreateBloodRequestDTO;
//import com.quyet.superapp.entity.Patient;
//import com.quyet.superapp.mapper.PatientMapper;
//import com.quyet.superapp.repository.PatientRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PatientService {
//
//    private final PatientRepository patientRepo;
//    private final PatientMapper patientMapper;
//
//    public Patient getOrCreateFromDTO(CreateBloodRequestDTO dto) {
//        if (dto.getCitizenId() != null) {
//            return patientRepo.findByCitizenId(dto.getCitizenId())
//                    .orElseGet(() -> patientRepo.save(patientMapper.toEntity(dto)));
//        }
//        return patientRepo.save(patientMapper.toEntity(dto));
//    }
//
//
//
//    // ⚠️ Tạo mới nếu chưa tồn tại
//    public Patient getOrCreateFromDTO(BloodRequestWithNewPatientDTO dto) {
//        if (dto.getCitizenId() != null) {
//            return patientRepo.findByCitizenId(dto.getCitizenId())
//                    .orElseGet(() -> patientRepo.save(patientMapper.toEntity(dto)));
//        }
//        return patientRepo.save(patientMapper.toEntity(dto));
//    }
//
//
//    // ✅ Tìm theo ID nếu được truyền từ suspectedPatientId
//    public Patient findById(Long id) {
//        return patientRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với ID = " + id));
//    }
//}
