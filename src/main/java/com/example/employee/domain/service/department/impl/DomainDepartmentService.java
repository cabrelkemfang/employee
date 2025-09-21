package com.example.employee.domain.service.department.impl;

import com.example.employee.domain.dto.DepartmentInfo;
import com.example.employee.domain.service.department.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of DepartmentService demonstrating concurrent operations
 */
@Service
@Slf4j
public class DomainDepartmentService implements DepartmentService {
    
    // Mock department data
    private static final Map<Long, DepartmentInfo> DEPARTMENTS = Map.of(
            1L, DepartmentInfo.builder().id(1L).name("Engineering").build(),
            2L, DepartmentInfo.builder().id(2L).name("Human Resources").build(),
            3L, DepartmentInfo.builder().id(3L).name("Finance").build(),
            4L, DepartmentInfo.builder().id(4L).name("Marketing").build(),
            5L, DepartmentInfo.builder().id(5L).name("Sales").build()
    );

    @Override
    public DepartmentInfo getDepartmentByEmployeeId(Long employeeId) {
        log.info("Fetching department for employee: {}", employeeId);
        
        // Simulate fetch time
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 400));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Department fetch interrupted", e);
        }
        
        // Mock logic to map employee to department
        Long departmentId = (employeeId % 5) + 1;
        return DEPARTMENTS.get(departmentId);
    }

    @Override
    public DepartmentInfo getDepartmentByEmployeeIdWithTimeout(Long employeeId, Duration timeout) {
        log.info("Fetching department for employee: {} with timeout: {}", employeeId, timeout);
        
        // Simulate a potentially long-running fetch
        try {
            long sleepTime = Math.min(timeout.toMillis() - 50, ThreadLocalRandom.current().nextInt(100, 1500));
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Department fetch interrupted", e);
        }
        
        return getDepartmentByEmployeeId(employeeId);
    }

    @Override
    public DepartmentInfo getDepartmentById(Long departmentId) {
        log.info("Fetching department by ID: {}", departmentId);
        
        // Simulate fetch time
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(50, 200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Department fetch interrupted", e);
        }
        
        return DEPARTMENTS.get(departmentId);
    }

    @Override
    public DepartmentInfo updateDepartment(DepartmentInfo departmentInfo) {
        log.info("Updating department: {}", departmentInfo);
        
        // Simulate update time
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(50, 300));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Department update interrupted", e);
        }
        
        return DepartmentInfo.builder()
                .id(departmentInfo.getId())
                .name(departmentInfo.getName())
                .build();
    }
}