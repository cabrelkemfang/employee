package com.example.employee.domain.service;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.exception.EmployeeAlreadyExistsException;
import com.example.employee.domain.exception.InvalidEmailFormatException;
import com.example.employee.domain.exception.InvalidPhoneNumberFormatException;
import com.example.employee.domain.port.EmployeeRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class EmployeeRequestValidation {
    private final EmployeeRepositoryPort employeeRepositoryPort;

    public void validate(EmployeeRequest employeeRequest) {
        // validate if  user id already present
//        var isPresent = isUserPresent(employeeRequest.getEmail());
//
//        if (!isPresent) {
//            throw new EmployeeAlreadyExistsException(employeeRequest);
//        }

        // Validate email
        if (!isValidEmail(employeeRequest.getEmail())) {
            throw new InvalidEmailFormatException(employeeRequest);
        }

        // Validate phone number (Mauritius)
        if (!isValidPhoneNumber(employeeRequest.getPhoneNumber())) {
            throw new InvalidPhoneNumberFormatException(employeeRequest);
        }
    }

    private boolean isUserPresent(String email) {
        return employeeRepositoryPort.isUserPresent(email);
    }

    // Email validation using regex
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    // Phone number validation for Mauritius format
    private static boolean isValidPhoneNumber(String phoneNumber) {
        // Mauritius phone numbers should be in the format: +230XXXXXXXX
        String phoneRegex = "^\\+230\\d{8}$";
        return phoneNumber != null && Pattern.matches(phoneRegex, phoneNumber);
    }
}
