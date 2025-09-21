package com.example.employee.domain.service.salary.impl;

import com.example.employee.domain.dto.Salary;
import com.example.employee.domain.service.salary.SalaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of SalaryService demonstrating concurrent operations
 */
@Service
@Slf4j
public class DomainSalaryService implements SalaryService {

    @Override
    public Salary calculateSalary(Long employeeId) {
        log.info("Calculating salary for employee: {}", employeeId);
        
        // Simulate calculation time
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Salary calculation interrupted", e);
        }
        
        // Mock salary calculation based on employee ID
        BigDecimal baseAmount = BigDecimal.valueOf(50000 + (employeeId * 1000));
        
        return Salary.builder()
                .id(employeeId * 100) // Mock salary ID
                .amount(baseAmount)
                .currency("USD")
                .build();
    }

    @Override
    public Salary calculateSalaryWithTimeout(Long employeeId, Duration timeout) {
        log.info("Calculating salary for employee: {} with timeout: {}", employeeId, timeout);
        
        // Simulate a potentially long-running calculation
        try {
            long sleepTime = Math.min(timeout.toMillis() - 50, ThreadLocalRandom.current().nextInt(100, 2000));
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Salary calculation interrupted", e);
        }
        
        return calculateSalary(employeeId);
    }

    @Override
    public boolean validateSalary(Salary salary) {
        log.info("Validating salary: {}", salary);
        
        if (salary == null) {
            return false;
        }
        
        if (salary.getAmount() == null || salary.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        if (salary.getCurrency() == null || salary.getCurrency().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }

    @Override
    public Salary updateSalary(Long employeeId, Salary salary) {
        log.info("Updating salary for employee: {} with new salary: {}", employeeId, salary);
        
        // Simulate update time
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(50, 200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Salary update interrupted", e);
        }
        
        return Salary.builder()
                .id(salary.getId())
                .amount(salary.getAmount())
                .currency(salary.getCurrency())
                .build();
    }
}