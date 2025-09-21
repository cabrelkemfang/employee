package com.example.employee.domain.service.salary;

import com.example.employee.domain.dto.Salary;
import java.time.Duration;

/**
 * Service interface for handling salary-related operations with structured concurrency
 */
public interface SalaryService {
    
    /**
     * Calculates the salary for an employee
     * 
     * @param employeeId the employee ID
     * @return the calculated salary
     */
    Salary calculateSalary(Long employeeId);
    
    /**
     * Calculates the salary for an employee with a timeout
     * 
     * @param employeeId the employee ID
     * @param timeout the maximum time to wait for calculation
     * @return the calculated salary
     */
    Salary calculateSalaryWithTimeout(Long employeeId, Duration timeout);
    
    /**
     * Validates salary information
     * 
     * @param salary the salary to validate
     * @return true if valid, false otherwise
     */
    boolean validateSalary(Salary salary);
    
    /**
     * Updates salary information
     * 
     * @param employeeId the employee ID
     * @param salary the new salary information
     * @return the updated salary
     */
    Salary updateSalary(Long employeeId, Salary salary);
}