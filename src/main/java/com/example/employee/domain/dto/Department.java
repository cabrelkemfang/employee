package com.example.employee.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Department {
    HR("Human Resources"),
    FINANCE("Finance"),
    ENGINEERING("Engineering"),
    MARKETING("Marketing"),
    SALES("Sales"),
    IT("Information Technology"),
    ADMINISTRATION("Administration"),
    CUSTOMER_SERVICE("Customer Service");

    private final String description;

    // Static method to get enum from name
    public static Department fromString(String name) {
        try {
            return Department.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("");
        }
    }
}
