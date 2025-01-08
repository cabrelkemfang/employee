package com.example.employee.domain.service.Impl;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.service.EmployeeRequestValidation;
import com.example.employee.domain.service.EmployeeService;

public class EmployeeServiceValidationDecorator extends EmployeeServiceDecorator {

    private final EmployeeRequestValidation employeeRequestValidation;

    public EmployeeServiceValidationDecorator(EmployeeService decoratedEmployeeService, EmployeeRequestValidation employeeRequestValidation) {
        super(decoratedEmployeeService);
        this.employeeRequestValidation = employeeRequestValidation;
    }

    @Override
    public void createEmployee(EmployeeRequest employeeRequest) {
        employeeRequestValidation.validate(employeeRequest);
        super.createEmployee(employeeRequest);
    }
}
