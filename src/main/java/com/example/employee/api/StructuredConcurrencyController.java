package com.example.employee.api;

import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.service.concurrency.StructuredConcurrencyEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

/**
 * REST controller demonstrating structured concurrency features
 */
@RestController
@RequestMapping("/api/employees/structured-concurrency")
@RequiredArgsConstructor
@Slf4j
public class StructuredConcurrencyController {

    private final StructuredConcurrencyEmployeeService structuredConcurrencyEmployeeService;

    @GetMapping(value = "/{employeeId}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployeeDetailsWithConcurrency(@PathVariable("employeeId") Long employeeId) {
        log.info("Getting employee details with concurrency for employee: {}", employeeId);
        
        Employee employee = structuredConcurrencyEmployeeService.getEmployeeDetailsWithConcurrency(employeeId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(employee);
    }

    @GetMapping(value = "/{employeeId}/details-with-timeout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployeeDetailsWithTimeout(
            @PathVariable("employeeId") Long employeeId,
            @RequestParam(value = "timeoutSeconds", defaultValue = "5") int timeoutSeconds) {
        log.info("Getting employee details with timeout {} seconds for employee: {}", timeoutSeconds, employeeId);
        
        Duration timeout = Duration.ofSeconds(timeoutSeconds);
        Employee employee = structuredConcurrencyEmployeeService.getEmployeeDetailsWithTimeout(employeeId, timeout);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(employee);
    }

    @PostMapping(value = "/batch-update", 
                 consumes = MediaType.APPLICATION_JSON_VALUE, 
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Employee>> updateEmployeesWithErrorHandling(@RequestBody List<Employee> employees) {
        log.info("Updating {} employees with error handling", employees.size());
        
        List<Employee> updatedEmployees = structuredConcurrencyEmployeeService.updateEmployeesWithErrorHandling(employees);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedEmployees);
    }

    @GetMapping(value = "/{employeeId}/first-valid-salary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployeeWithFirstValidSalary(@PathVariable("employeeId") Long employeeId) {
        log.info("Getting employee with first valid salary for employee: {}", employeeId);
        
        Employee employee = structuredConcurrencyEmployeeService.getEmployeeWithFirstValidSalary(employeeId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(employee);
    }
}