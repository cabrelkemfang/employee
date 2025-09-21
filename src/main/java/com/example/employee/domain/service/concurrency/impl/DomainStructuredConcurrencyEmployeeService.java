package com.example.employee.domain.service.concurrency.impl;

import com.example.employee.domain.dto.DepartmentInfo;
import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.dto.Salary;
import com.example.employee.domain.service.concurrency.StructuredConcurrencyEmployeeService;
import com.example.employee.domain.service.department.DepartmentService;
import com.example.employee.domain.service.repository.EmployeeService;
import com.example.employee.domain.service.salary.SalaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of StructuredConcurrencyEmployeeService demonstrating Java 21's StructuredTaskScope
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DomainStructuredConcurrencyEmployeeService implements StructuredConcurrencyEmployeeService {

    private final EmployeeService employeeService;
    private final SalaryService salaryService;
    private final DepartmentService departmentService;

    @Override
    public Employee getEmployeeDetailsWithConcurrency(Long employeeId) {
        log.info("Fetching employee details with concurrency for employee: {}", employeeId);
        
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Submit concurrent tasks for fetching employee data
            var employeeTask = scope.fork(() -> employeeService.findEmployeeById(employeeId));
            var salaryTask = scope.fork(() -> salaryService.calculateSalary(employeeId));
            var departmentTask = scope.fork(() -> departmentService.getDepartmentByEmployeeId(employeeId));
            
            // Wait for all tasks to complete or any to fail
            scope.join();           // Wait for all tasks to finish
            scope.throwIfFailed();  // Throw if any task failed
            
            // Combine results
            Employee employee = employeeTask.get();
            Salary salary = salaryTask.get();
            DepartmentInfo departmentInfo = departmentTask.get();
            
            // Update employee with concurrent results
            employee.setSalary(salary);
            employee.setDepartmentInfo(departmentInfo);
            
            log.info("Successfully fetched employee details with concurrency for employee: {}", employeeId);
            return employee;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Employee fetch interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to fetch employee details", e);
        }
    }

    @Override
    public Employee getEmployeeDetailsWithTimeout(Long employeeId, Duration timeout) {
        log.info("Fetching employee details with timeout {} for employee: {}", timeout, employeeId);
        
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Submit concurrent tasks with timeout considerations
            var employeeTask = scope.fork(() -> employeeService.findEmployeeById(employeeId));
            var salaryTask = scope.fork(() -> salaryService.calculateSalaryWithTimeout(employeeId, timeout));
            var departmentTask = scope.fork(() -> departmentService.getDepartmentByEmployeeIdWithTimeout(employeeId, timeout));
            
            // Wait for all tasks to complete or timeout
            scope.joinUntil(java.time.Instant.now().plus(timeout));
            scope.throwIfFailed();
            
            // Combine results
            Employee employee = employeeTask.get();
            Salary salary = salaryTask.get();
            DepartmentInfo departmentInfo = departmentTask.get();
            
            employee.setSalary(salary);
            employee.setDepartmentInfo(departmentInfo);
            
            log.info("Successfully fetched employee details with timeout for employee: {}", employeeId);
            return employee;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Employee fetch interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to fetch employee details within timeout", e);
        } catch (TimeoutException e) {
            throw new RuntimeException("Timeout exceeded while fetching employee details", e);
        }
    }

    @Override
    public List<Employee> updateEmployeesWithErrorHandling(List<Employee> employees) {
        log.info("Updating {} employees with error handling", employees.size());
        
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Submit concurrent update tasks
            List<StructuredTaskScope.Subtask<Employee>> updateTasks = new ArrayList<>();
            
            for (Employee employee : employees) {
                var updateTask = scope.fork(() -> {
                    // Simulate update operations that might fail
                    if (ThreadLocalRandom.current().nextBoolean() && employee.getEmployeeId() % 10 == 0) {
                        throw new RuntimeException("Simulated update failure for employee: " + employee.getEmployeeId());
                    }
                    
                    // Update salary and department concurrently
                    if (employee.getSalary() != null) {
                        var updatedSalary = salaryService.updateSalary(employee.getEmployeeId(), employee.getSalary());
                        employee.setSalary(updatedSalary);
                    }
                    
                    if (employee.getDepartmentInfo() != null) {
                        var updatedDepartment = departmentService.updateDepartment(employee.getDepartmentInfo());
                        employee.setDepartmentInfo(updatedDepartment);
                    }
                    
                    return employee;
                });
                updateTasks.add(updateTask);
            }
            
            // Wait for all tasks to complete or any to fail
            scope.join();
            scope.throwIfFailed();  // This will fail fast if any update fails
            
            // Collect results
            List<Employee> updatedEmployees = new ArrayList<>();
            for (var task : updateTasks) {
                updatedEmployees.add(task.get());
            }
            
            log.info("Successfully updated {} employees", updatedEmployees.size());
            return updatedEmployees;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Employee updates interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to update employees", e);
        }
    }

    @Override
    public Employee getEmployeeWithFirstValidSalary(Long employeeId) {
        log.info("Getting employee with first valid salary for employee: {}", employeeId);
        
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Employee>()) {
            // Submit multiple concurrent salary calculation attempts
            // This demonstrates ShutdownOnSuccess - first successful result wins
            
            scope.fork(() -> {
                // First calculation method
                var employee = employeeService.findEmployeeById(employeeId);
                var salary = salaryService.calculateSalary(employeeId);
                var departmentInfo = departmentService.getDepartmentByEmployeeId(employeeId);
                
                employee.setSalary(salary);
                employee.setDepartmentInfo(departmentInfo);
                return employee;
            });
            
            scope.fork(() -> {
                // Alternative calculation method (slightly delayed)
                Thread.sleep(100);
                var employee = employeeService.findEmployeeById(employeeId);
                var salary = salaryService.calculateSalary(employeeId);
                salary.setAmount(salary.getAmount().multiply(java.math.BigDecimal.valueOf(1.1))); // 10% bonus
                var departmentInfo = departmentService.getDepartmentByEmployeeId(employeeId);
                
                employee.setSalary(salary);
                employee.setDepartmentInfo(departmentInfo);
                return employee;
            });
            
            scope.fork(() -> {
                // Third calculation method (even more delayed)
                Thread.sleep(200);
                var employee = employeeService.findEmployeeById(employeeId);
                var salary = salaryService.calculateSalary(employeeId);
                salary.setAmount(salary.getAmount().multiply(java.math.BigDecimal.valueOf(0.9))); // 10% reduction
                var departmentInfo = departmentService.getDepartmentByEmployeeId(employeeId);
                
                employee.setSalary(salary);
                employee.setDepartmentInfo(departmentInfo);
                return employee;
            });
            
            // Wait for first successful result
            scope.join();
            
            Employee result = scope.result();
            log.info("Successfully got employee with first valid salary for employee: {}", employeeId);
            return result;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Employee fetch interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to get employee with first valid salary", e);
        }
    }
}