package com.example.employee.api.mapper;

import com.example.employee.api.dto.EmployeeRequest;
import com.example.employee.api.dto.EmployeeResponse;
import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.dto.PaginatedResponse;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeResponseMapper {
    PaginatedResponse<EmployeeResponse> mapToPaginatedResponse(PaginatedResponse<Employee> paginatedResponse);

    EmployeeResponse mapToEmployeeResponse(Employee employee);

    Employee mapToEmployee(EmployeeRequest employeeRequest);
}
