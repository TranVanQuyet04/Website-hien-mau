package com.quyet.superapp.controller;

import com.quyet.superapp.entity.Report;
import com.quyet.superapp.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor

public class ReportController {

    @Autowired
    private ReportService service;

    @GetMapping
    public List<Report> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Report create(@RequestBody Report report) {
        return service.create(report);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> update(@PathVariable Long id, @RequestBody Report report) {
        Report updated = service.update(id, report);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}

