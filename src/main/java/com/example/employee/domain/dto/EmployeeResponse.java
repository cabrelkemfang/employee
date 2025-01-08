package com.example.employee.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmployeeResponse(Long employeeId,
                               String firstName,
                               String lastName,
                               String email,
                               String phoneNumber,
                               Department department,
                               LocalDateTime localDateTime) {
}
