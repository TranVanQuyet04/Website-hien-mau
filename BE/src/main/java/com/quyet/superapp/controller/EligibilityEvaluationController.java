package com.quyet.superapp.controller;

import com.quyet.superapp.service.DonationRegistrationService;
import com.quyet.superapp.service.EligibilityEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
public class EligibilityEvaluationController {

    private final EligibilityEvaluationService eligibilityEvaluationService;

    @PostMapping("/evaluate/{registrationId}")
    public ResponseEntity<String> evaluateAndCreate(@PathVariable Long registrationId) {
        String result = eligibilityEvaluationService.evaluateAndCreateDonation(registrationId);
        return ResponseEntity.ok(result);
    }
}
