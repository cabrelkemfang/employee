package com.example.employee.infra.mapper;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.dto.EmployeeResponse;
import com.example.employee.infra.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmployeeMapper {

    EmployeeResponse mapToEmployeeResponse(Employee employee);

    @Mapping(target = "localDateTime", expression = "java(java.time.LocalDateTime.now())")
    Employee mapToEmployee(EmployeeRequest employeeRequest);
}
