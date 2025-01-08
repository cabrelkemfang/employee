package com.example.employee.domain.exception;

import com.example.employee.domain.dto.EmployeeRequest;

public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(EmployeeRequest employee) {
        super("Employee with email " + employee.getEmail() + " already exists.");
    }
}
