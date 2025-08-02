package com.quyet.superapp.controller;

import com.quyet.superapp.entity.CompatibilityRule;
import com.quyet.superapp.service.CompatibilityRuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compatibility-rules")
@CrossOrigin(origins = "http://localhost:5713")
public class CompatibilityRuleController {

    @Autowired
    private CompatibilityRuleService service;

    @GetMapping
    public List<CompatibilityRule> getAll() {
        return service.getAllRules();
    }

    @GetMapping("/filter")
    public List<CompatibilityRule> getCompatibleRules(
            @RequestParam String recipientType,
            @RequestParam String component
    ) {
        return service.getCompatibleRules(recipientType, component);
    }

    @PostMapping("/create")
    public CompatibilityRule create(@RequestBody CompatibilityRule rule) {
        return service.addRule(rule);
    }

    @PutMapping("/{id}")
    public CompatibilityRule update(@PathVariable Long id, @RequestBody CompatibilityRule rule) {
        return service.updateRule(id, rule);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteRule(id);
    }
}
