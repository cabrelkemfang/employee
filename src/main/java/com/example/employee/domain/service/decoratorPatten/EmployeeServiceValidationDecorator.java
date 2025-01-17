package com.example.employee.domain.service.decoratorPatten;

import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.service.repository.EmployeeService;

public class EmployeeServiceValidationDecorator extends EmployeeServiceDecorator {

    private final EmployeeRequestValidation employeeRequestValidation;

    public EmployeeServiceValidationDecorator(EmployeeService decoratedEmployeeService,
                                              EmployeeRequestValidation employeeRequestValidation) {
        super(decoratedEmployeeService);
        this.employeeRequestValidation = employeeRequestValidation;
    }

    @Override
    public void createEmployee(Employee employee) {
        employeeRequestValidation.validate(employee);
        super.createEmployee(employee);
    }
}
