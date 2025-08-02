//package com.quyet.superapp.controller;
//
//import com.quyet.superapp.dto.BloodComponentDTO;
//import com.quyet.superapp.dto.BloodTypeDTO;
//import com.quyet.superapp.entity.BloodType;
//import com.quyet.superapp.mapper.BloodTypeMapper;
//import com.quyet.superapp.service.BloodComponentService;
//import com.quyet.superapp.service.BloodTypeService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/blood-components")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173")
//@Validated
//public class BloodComponentController {
//
//    private final BloodComponentService bloodComponentService;
//
//    @GetMapping("/full")
//    public ResponseEntity<List<BloodComponentDTO>> getFullComponents() {
//        return ResponseEntity.ok(bloodComponentService.getAll());
//    }
//
//
//
//
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<BloodComponentDTO> create(@Validated @RequestBody BloodComponentDTO dto) {
//        return ResponseEntity.ok(bloodComponentService.create(dto));
//    }
//
//    @GetMapping("/available")
//    public ResponseEntity<List<BloodComponentDTO>> getAvailableComponents() {
//        return ResponseEntity.ok(bloodComponentService.getAvailableComponents());
//    }
//
//    @GetMapping
//    public ResponseEntity<List<BloodComponentDTO>> getAllComponents() {
//        return ResponseEntity.ok(bloodComponentService.getAll());
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<BloodComponentDTO> update(@PathVariable Long id, @RequestBody BloodComponentDTO dto) {
//        return ResponseEntity.ok(bloodComponentService.update(id, dto));
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        bloodComponentService.delete(id);
//        return ResponseEntity.ok().build();
//    }
//}


package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.dto.BloodComponentUpdateDTO;
import com.quyet.superapp.service.BloodComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-components")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class BloodComponentController {

    private final BloodComponentService bloodComponentService;

    /**
     * 📄 Lấy danh sách toàn bộ thành phần máu (id, name, description, isActive)
     */
    @GetMapping("/full")
    public ResponseEntity<List<BloodComponentDTO>> getFullComponents() {
        return ResponseEntity.ok(bloodComponentService.getAll());
    }

    /**
     * 📄 Lấy danh sách thành phần máu đang active
     */
    @GetMapping("/available")
    public ResponseEntity<List<BloodComponentDTO>> getAvailableComponents() {
        return ResponseEntity.ok(bloodComponentService.getAvailableComponents());
    }

    /**
     * 📄 Lấy danh sách đơn giản (tương đương getFull)
     */
    @GetMapping
    public ResponseEntity<List<BloodComponentDTO>> getAllComponents() {
        return ResponseEntity.ok(bloodComponentService.getAll());
    }

    /**
     * ➕ Thêm mới thành phần máu
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodComponentDTO> create(
            @Validated @RequestBody BloodComponentDTO dto) {
        BloodComponentDTO created = bloodComponentService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * ✏️ Cập nhật thành phần máu
     */
    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodComponentDTO> update(
            @PathVariable Long id,
            @RequestBody BloodComponentUpdateDTO dto) {
        return ResponseEntity.ok(bloodComponentService.partialUpdate(id, dto));
    }


    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        bloodComponentService.deactivate(id);
        return ResponseEntity.ok("Đã vô hiệu hóa thành phần máu");
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<?> reactivate(@PathVariable Long id) {
        bloodComponentService.reactivate(id);
        return ResponseEntity.ok("Đã khôi phục thành phần máu");
    }

}
