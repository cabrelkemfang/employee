package com.example.employee.domain.service.repository;

import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.dto.PaginatedResponse;

public interface EmployeeService {
    PaginatedResponse<Employee> getEmployees(int page, int size);

    Employee findEmployeeById(Long employeeId);

    void createEmployee(Employee employee);
}
