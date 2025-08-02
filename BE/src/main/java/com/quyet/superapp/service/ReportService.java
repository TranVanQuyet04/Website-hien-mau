package com.quyet.superapp.service;

import com.quyet.superapp.entity.Report;
import com.quyet.superapp.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    public Optional<Report> getById(Long id) {
        return reportRepository.findById(id);
    }

    public Report create(Report report) {
        if (report.getCreatedAt() == null) {
            report.setCreatedAt(LocalDateTime.now());
        }
        return reportRepository.save(report);
    }

    public void deleteById(Long id) {
        reportRepository.deleteById(id);
    }

    public Report update(Long id, Report updatedReport) {
        return reportRepository.findById(id)
                .map(existing -> {
                    existing.setReportType(updatedReport.getReportType());
                    existing.setContent(updatedReport.getContent());
                    existing.setGeneratedBy(updatedReport.getGeneratedBy());
                    existing.setCreatedAt(updatedReport.getCreatedAt());
                    return reportRepository.save(existing);
                })
                .orElse(null);
    }
}