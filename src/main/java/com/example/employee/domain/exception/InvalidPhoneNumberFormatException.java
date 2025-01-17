package com.example.employee.domain.exception;

import com.example.employee.domain.dto.Employee;

public class InvalidPhoneNumberFormatException extends RuntimeException {

    public InvalidPhoneNumberFormatException(Employee employeeRequest) {
        super("Invalid phone number format " + employeeRequest.getPhoneNumber());
    }
}
