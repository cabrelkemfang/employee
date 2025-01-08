package com.example.employee.config;

import com.example.employee.domain.dto.Department;
import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.dto.EmployeeResponse;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableTransformer {

    @DataTableType
    public EmployeeRequest employeeRequestEntryTransformer(Map<String, String> row) {
        return EmployeeRequest.builder()
                .email(row.get("Email"))
                .phoneNumber(row.get("Phone Number"))
                .department(Department.fromString(row.get("Department")))
                .firstName(row.get("First Name"))
                .lastName(row.get("Last Name"))
                .build();
    }

    @DataTableType
    public EmployeeResponse employeeResponseEntryTransformer(Map<String, String> row) {
        return EmployeeResponse.builder()
                .employeeId(Long.valueOf(row.get("Employee Id")))
                .email(row.get("Email"))
                .phoneNumber(row.get("Phone Number"))
                .department(Department.fromString(row.get("Department")))
                .firstName(row.get("First Name"))
                .lastName(row.get("Last Name"))
                .build();
    }
}
