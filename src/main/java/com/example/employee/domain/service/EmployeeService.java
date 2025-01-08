package com.example.employee.domain.service;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.dto.EmployeeResponse;
import com.example.employee.domain.dto.PaginatedResponse;

public interface EmployeeService {
    PaginatedResponse<EmployeeResponse> getEmployees(int page, int size);

    EmployeeResponse findEmployeeById(Long employeeId);

    void createEmployee(EmployeeRequest employeeRequest);
}
