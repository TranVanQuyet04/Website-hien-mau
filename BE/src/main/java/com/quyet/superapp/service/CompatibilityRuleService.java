package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.CompatibilityRule;
import com.quyet.superapp.repository.CompatibilityRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompatibilityRuleService {
    private final CompatibilityRuleRepository ruleRepo;

    public List<CompatibilityRule> getAllRules() {
        return ruleRepo.findAll();
    }

    public CompatibilityRule addRule(CompatibilityRule rule) {
        return ruleRepo.save(rule);
    }

    public CompatibilityRule updateRule(Long id, CompatibilityRule updated) {
        return ruleRepo.findById(id)
                .map(rule -> {
                    rule.setRecipientType(updated.getRecipientType());
                    rule.setDonorType(updated.getDonorType());
                    rule.setComponent(updated.getComponent());
                    rule.setIsCompatible(updated.getIsCompatible());
                    return ruleRepo.save(rule);
                }).orElse(null);
    }

    public void deleteRule(Long id) {
        ruleRepo.deleteById(id);
    }

    public List<CompatibilityRule> getCompatibleRules(String recipientType, String component) {
        return ruleRepo.findByRecipientType_DescriptionAndComponent_NameAndIsCompatibleTrue(recipientType, component);
    }

    public List<BloodType> getCompatibleDonors(String recipientType, String component) {
        return ruleRepo.findByRecipientType_DescriptionAndComponent_NameAndIsCompatibleTrue(recipientType, component)
                .stream()
                .map(CompatibilityRule::getDonorType)
                .distinct()
                .toList();
    }


}
