package com.quyet.superapp.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}