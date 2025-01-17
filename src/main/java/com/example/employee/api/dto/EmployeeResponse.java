package com.example.employee.api.dto;

import com.example.employee.domain.dto.Department;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmployeeResponse(Long employeeId,
                               String firstName,
                               String lastName,
                               String email,
                               Department department,
                               LocalDateTime localDateTime) {
}
