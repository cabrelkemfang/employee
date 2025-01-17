package com.example.employee.domain.exception;

import com.example.employee.domain.dto.Employee;

public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(Employee employee) {
        super("Employee with email " + employee.getEmail() + " already exists.");
    }
}
