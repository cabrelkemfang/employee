package com.example.employee.domain.port;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.dto.EmployeeResponse;
import com.example.employee.domain.dto.PaginatedResponse;

public interface EmployeeRepositoryPort {

    PaginatedResponse<EmployeeResponse> getEmployees(int page, int size);

    void createEmployee(EmployeeRequest employeeRequest);

    EmployeeResponse findEmployeeById(Long employeeId);

    boolean isUserPresent(String email);

    void deleteEmployeeById(Long employeeId);
}
