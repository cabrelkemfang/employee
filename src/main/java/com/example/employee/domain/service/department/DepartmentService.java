package com.example.employee.domain.service.department;

import com.example.employee.domain.dto.DepartmentInfo;
import java.time.Duration;

/**
 * Service interface for handling department-related operations with structured concurrency
 */
public interface DepartmentService {
    
    /**
     * Fetches department information by employee ID
     * 
     * @param employeeId the employee ID
     * @return the department information
     */
    DepartmentInfo getDepartmentByEmployeeId(Long employeeId);
    
    /**
     * Fetches department information by employee ID with a timeout
     * 
     * @param employeeId the employee ID
     * @param timeout the maximum time to wait for fetch operation
     * @return the department information
     */
    DepartmentInfo getDepartmentByEmployeeIdWithTimeout(Long employeeId, Duration timeout);
    
    /**
     * Fetches department information by department ID
     * 
     * @param departmentId the department ID
     * @return the department information
     */
    DepartmentInfo getDepartmentById(Long departmentId);
    
    /**
     * Updates department information
     * 
     * @param departmentInfo the department information to update
     * @return the updated department information
     */
    DepartmentInfo updateDepartment(DepartmentInfo departmentInfo);
}