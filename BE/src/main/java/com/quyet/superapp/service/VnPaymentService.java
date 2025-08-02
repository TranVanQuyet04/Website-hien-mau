package com.quyet.superapp.service;

import com.quyet.superapp.entity.VnPayment;
import com.quyet.superapp.repository.VnPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VnPaymentService {

    private final VnPaymentRepository repository;

    public List<VnPayment> getAll() {
        return repository.findAll();
    }

    public Optional<VnPayment> getById(Long id) {
        return repository.findById(id);
    }

    public VnPayment save(VnPayment payment) {
        return repository.save(payment);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
