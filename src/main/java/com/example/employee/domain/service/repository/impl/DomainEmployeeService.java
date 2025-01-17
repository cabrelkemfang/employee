package com.example.employee.domain.service.repository.impl;

import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.dto.PaginatedResponse;
import com.example.employee.domain.port.EmployeeRepositoryPort;
import com.example.employee.domain.service.repository.EmployeeService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DomainEmployeeService implements EmployeeService {

    private final EmployeeRepositoryPort employeeRepositoryPort;

    @Override
    public PaginatedResponse<Employee> getEmployees(int page, int size) {
        return employeeRepositoryPort.getEmployees(page, size);
    }

    @Override
    public Employee findEmployeeById(Long employeeId) {
        return employeeRepositoryPort.findEmployeeById(employeeId);
    }

    @Override
    public void createEmployee(Employee employee) {
        employeeRepositoryPort.createEmployee(employee);
    }
}
