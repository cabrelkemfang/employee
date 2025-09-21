package com.example.employee.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Department department;
    private Bank bank;
    private LocalDateTime localDateTime;
    
    // New fields for structured concurrency demo
    private Salary salary;
    private DepartmentInfo departmentInfo;
}