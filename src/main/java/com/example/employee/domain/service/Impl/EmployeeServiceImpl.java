package com.example.employee.domain.service.Impl;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.dto.EmployeeResponse;
import com.example.employee.domain.dto.PaginatedResponse;
import com.example.employee.domain.port.EmployeeRepositoryPort;
import com.example.employee.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepositoryPort employeeRepositoryPort;

    @Override
    public PaginatedResponse<EmployeeResponse> getEmployees(int page, int size) {
        return employeeRepositoryPort.getEmployees(page, size);
    }

    @Override
    public EmployeeResponse findEmployeeById(Long employeeId) {
        return employeeRepositoryPort.findEmployeeById(employeeId);
    }

    @Override
    public void createEmployee(EmployeeRequest employeeRequest) {
        employeeRepositoryPort.createEmployee(employeeRequest);
    }
}
