package com.example.employee.domain.exception;

import com.example.employee.domain.dto.EmployeeRequest;

public class InvalidEmailFormatException extends RuntimeException {

    public InvalidEmailFormatException(EmployeeRequest employeeRequest) {
        super("Invalid email format: " + employeeRequest.getEmail());
    }
}
