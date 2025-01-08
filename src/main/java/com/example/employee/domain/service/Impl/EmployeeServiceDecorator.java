package com.example.employee.domain.service.Impl;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.dto.EmployeeResponse;
import com.example.employee.domain.dto.PaginatedResponse;
import com.example.employee.domain.service.EmployeeService;

public abstract class EmployeeServiceDecorator implements EmployeeService {

    protected final EmployeeService decoratedEmployeeService;

    protected EmployeeServiceDecorator(EmployeeService decoratedEmployeeService) {
        this.decoratedEmployeeService = decoratedEmployeeService;
    }

    @Override
    public PaginatedResponse<EmployeeResponse> getEmployees(int page, int size) {
        return decoratedEmployeeService.getEmployees(page, size);
    }

    @Override
    public EmployeeResponse findEmployeeById(Long employeeId) {
        return decoratedEmployeeService.findEmployeeById(employeeId);
    }

    @Override
    public void createEmployee(EmployeeRequest employeeRequest) {
        decoratedEmployeeService.createEmployee(employeeRequest);
    }
}
