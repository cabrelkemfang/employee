package com.example.employee.domain.port;

import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.dto.PaginatedResponse;

public interface EmployeeRepositoryPort {

    PaginatedResponse<Employee> getEmployees(int page, int size);

    void createEmployee(Employee employee);

    Employee findEmployeeById(Long employeeId);

    boolean isUserPresent(String email);

    void deleteEmployeeById(Long employeeId);
}
