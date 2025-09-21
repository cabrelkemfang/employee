package com.example.employee.domain.service.concurrency;

import com.example.employee.domain.dto.Employee;
import java.time.Duration;
import java.util.List;

/**
 * Service interface for structured concurrency operations
 */
public interface StructuredConcurrencyEmployeeService {
    
    /**
     * Fetches employee details with parallel salary and department lookups using StructuredTaskScope
     * 
     * @param employeeId the employee ID
     * @return complete employee details
     */
    Employee getEmployeeDetailsWithConcurrency(Long employeeId);
    
    /**
     * Fetches employee details with timeout management using StructuredTaskScope
     * 
     * @param employeeId the employee ID
     * @param timeout the maximum time to wait for all operations
     * @return complete employee details
     */
    Employee getEmployeeDetailsWithTimeout(Long employeeId, Duration timeout);
    
    /**
     * Performs parallel employee updates with error handling using ShutdownOnFailure
     * 
     * @param employees list of employees to update
     * @return list of updated employees
     */
    List<Employee> updateEmployeesWithErrorHandling(List<Employee> employees);
    
    /**
     * Demonstrates ShutdownOnSuccess scenario - finds first valid salary calculation
     * 
     * @param employeeId the employee ID
     * @return the first successfully calculated salary
     */
    Employee getEmployeeWithFirstValidSalary(Long employeeId);
}