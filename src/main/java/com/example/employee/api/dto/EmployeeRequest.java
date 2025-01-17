package com.example.employee.api.dto;

import com.example.employee.domain.dto.Bank;
import com.example.employee.domain.dto.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Department department;
    private Bank bank;
}