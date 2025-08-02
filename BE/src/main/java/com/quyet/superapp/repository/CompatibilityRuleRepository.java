package com.quyet.superapp.repository;

import com.quyet.superapp.entity.CompatibilityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CompatibilityRuleRepository extends JpaRepository<CompatibilityRule, Long> {
    List<CompatibilityRule> findByRecipientType_DescriptionAndComponent_NameAndIsCompatibleTrue(String recipientTypeDesc, String componentName);

}