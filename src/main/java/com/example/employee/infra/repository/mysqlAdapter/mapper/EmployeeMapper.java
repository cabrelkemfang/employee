package com.example.employee.infra.repository.mysqlAdapter.mapper;

import com.example.employee.domain.dto.Employee;
import com.example.employee.infra.repository.mysqlAdapter.entity.EmployeeEntity;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper {

    Employee mapToEmployeeResponse(EmployeeEntity employeeEntity);

    EmployeeEntity mapToEmployee(Employee employee);
}
