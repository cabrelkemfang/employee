package com.example.employee.domain.exception;

import com.example.employee.domain.dto.EmployeeRequest;

public class InvalidPhoneNumberFormatException extends RuntimeException {

    public InvalidPhoneNumberFormatException(EmployeeRequest employeeRequest) {
        super("Invalid phone number format " + employeeRequest.getPhoneNumber());
    }
}
