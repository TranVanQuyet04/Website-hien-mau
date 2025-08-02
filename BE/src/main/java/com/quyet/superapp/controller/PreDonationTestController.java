package com.quyet.superapp.controller;

import com.quyet.superapp.dto.PreDonationTestDTO;
import com.quyet.superapp.service.PreDonationTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pre-donation-tests")
public class PreDonationTestController {

    @Autowired
    private PreDonationTestService preDonationTestService;

    // 📌 Tạo mới xét nghiệm trước hiến máu
    @PostMapping
    public ResponseEntity<PreDonationTestDTO> createPreDonationTest(@RequestBody PreDonationTestDTO dto) {
        return ResponseEntity.ok(preDonationTestService.create(dto));
    }

    // 📌 Lấy toàn bộ danh sách xét nghiệm
    @GetMapping
    public ResponseEntity<List<PreDonationTestDTO>> getAll() {
        return ResponseEntity.ok(preDonationTestService.getAll());
    }

    // 📌 Lấy xét nghiệm theo ID
    @GetMapping("/{id}") // id của người hiến máu
    public ResponseEntity<PreDonationTestDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(preDonationTestService.getById(id));
    }

    // 📌 Cập nhật xét nghiệm
    @PutMapping("/{id}")
    public ResponseEntity<PreDonationTestDTO> update(@PathVariable Long id, @RequestBody PreDonationTestDTO dto) {
        dto.setPreDonationTestId(id);
        return ResponseEntity.ok(preDonationTestService.update(dto));
    }

    // 📌 Xoá xét nghiệm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        preDonationTestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 📌 Kiểm tra 1 đơn hiến máu đã có xét nghiệm chưa
    @GetMapping("/exists/{donationId}")
    public ResponseEntity<Boolean> existsByDonationId(@PathVariable Long donationId) {
        return ResponseEntity.ok(preDonationTestService.existsByDonationId(donationId));
    }
}
