package com.example.employee.domain.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long employeeId) {
        super("Employee not Found " + employeeId);
    }
}
