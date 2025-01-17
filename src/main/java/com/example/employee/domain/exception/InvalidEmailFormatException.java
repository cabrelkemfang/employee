package com.example.employee.domain.exception;

import com.example.employee.domain.dto.Employee;

public class InvalidEmailFormatException extends RuntimeException {

    public InvalidEmailFormatException(Employee employeeRequest) {
        super("Invalid email format: " + employeeRequest.getEmail());
    }
}
