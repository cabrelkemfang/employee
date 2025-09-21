package com.example.employee.domain.service.concurrency;

import com.example.employee.domain.dto.DepartmentInfo;
import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.dto.Salary;
import com.example.employee.domain.service.concurrency.impl.DomainStructuredConcurrencyEmployeeService;
import com.example.employee.domain.service.department.DepartmentService;
import com.example.employee.domain.service.repository.EmployeeService;
import com.example.employee.domain.service.salary.SalaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for DomainStructuredConcurrencyEmployeeService using FIRST principles and Given-When-Then structure
 */
@ExtendWith(MockitoExtension.class)
class DomainStructuredConcurrencyEmployeeServiceTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private SalaryService salaryService;

    @Mock
    private DepartmentService departmentService;

    private DomainStructuredConcurrencyEmployeeService structuredConcurrencyService;
    private PodamFactory podamFactory;

    @BeforeEach
    void setUp() {
        // Given: Set up the service and test data factory
        structuredConcurrencyService = new DomainStructuredConcurrencyEmployeeService(
                employeeService, salaryService, departmentService);
        podamFactory = new PodamFactoryImpl();
    }

    @Test
    @DisplayName("should get employee details with concurrency when all services succeed")
    void shouldGetEmployeeDetailsWithConcurrencyWhenAllServicesSucceed() {
        // Given: Mock responses from all services
        Long employeeId = 1L;
        Employee mockEmployee = podamFactory.manufacturePojo(Employee.class);
        mockEmployee.setEmployeeId(employeeId);
        
        Salary mockSalary = Salary.builder()
                .id(100L)
                .amount(BigDecimal.valueOf(75000))
                .currency("USD")
                .build();
                
        DepartmentInfo mockDepartment = DepartmentInfo.builder()
                .id(2L)
                .name("Engineering")
                .build();

        when(employeeService.findEmployeeById(employeeId)).thenReturn(mockEmployee);
        when(salaryService.calculateSalary(employeeId)).thenReturn(mockSalary);
        when(departmentService.getDepartmentByEmployeeId(employeeId)).thenReturn(mockDepartment);

        // When: Getting employee details with concurrency
        Employee result = structuredConcurrencyService.getEmployeeDetailsWithConcurrency(employeeId);

        // Then: Should return complete employee details
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo(employeeId);
        assertThat(result.getSalary()).isEqualTo(mockSalary);
        assertThat(result.getDepartmentInfo()).isEqualTo(mockDepartment);
    }

    @Test
    @DisplayName("should throw exception when employee service fails")
    void shouldThrowExceptionWhenEmployeeServiceFails() {
        // Given: Employee service throws exception
        Long employeeId = 1L;
        when(employeeService.findEmployeeById(employeeId))
                .thenThrow(new RuntimeException("Employee not found"));
        
        // Setup other services to succeed
        when(salaryService.calculateSalary(employeeId))
                .thenReturn(podamFactory.manufacturePojo(Salary.class));
        when(departmentService.getDepartmentByEmployeeId(employeeId))
                .thenReturn(podamFactory.manufacturePojo(DepartmentInfo.class));

        // When & Then: Should throw RuntimeException
        assertThatThrownBy(() -> structuredConcurrencyService.getEmployeeDetailsWithConcurrency(employeeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch employee details");
    }

    @Test
    @DisplayName("should get employee details with timeout when timeout is sufficient")
    void shouldGetEmployeeDetailsWithTimeoutWhenTimeoutIsSufficient() {
        // Given: Mock responses from all services and sufficient timeout
        Long employeeId = 2L;
        Duration timeout = Duration.ofSeconds(5);
        
        Employee mockEmployee = podamFactory.manufacturePojo(Employee.class);
        mockEmployee.setEmployeeId(employeeId);
        
        Salary mockSalary = podamFactory.manufacturePojo(Salary.class);
        DepartmentInfo mockDepartment = podamFactory.manufacturePojo(DepartmentInfo.class);

        when(employeeService.findEmployeeById(employeeId)).thenReturn(mockEmployee);
        when(salaryService.calculateSalaryWithTimeout(eq(employeeId), any(Duration.class))).thenReturn(mockSalary);
        when(departmentService.getDepartmentByEmployeeIdWithTimeout(eq(employeeId), any(Duration.class))).thenReturn(mockDepartment);

        // When: Getting employee details with timeout
        Employee result = structuredConcurrencyService.getEmployeeDetailsWithTimeout(employeeId, timeout);

        // Then: Should return complete employee details
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo(employeeId);
        assertThat(result.getSalary()).isEqualTo(mockSalary);
        assertThat(result.getDepartmentInfo()).isEqualTo(mockDepartment);
    }

    @Test
    @DisplayName("should update employees with error handling when all updates succeed")
    void shouldUpdateEmployeesWithErrorHandlingWhenAllUpdatesSucceed() {
        // Given: List of employees with valid IDs (not multiples of 10 to avoid simulated failures)
        Employee employee1 = createTestEmployee(1L);
        Employee employee2 = createTestEmployee(3L);
        List<Employee> employees = Arrays.asList(employee1, employee2);

        // Mock successful updates
        when(salaryService.updateSalary(eq(1L), any(Salary.class))).thenReturn(employee1.getSalary());
        when(salaryService.updateSalary(eq(3L), any(Salary.class))).thenReturn(employee2.getSalary());
        when(departmentService.updateDepartment(any(DepartmentInfo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When: Updating employees with error handling
        List<Employee> result = structuredConcurrencyService.updateEmployeesWithErrorHandling(employees);

        // Then: Should return updated employees
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmployeeId()).isEqualTo(1L);
        assertThat(result.get(1).getEmployeeId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("should get employee with first valid salary when first attempt succeeds")
    void shouldGetEmployeeWithFirstValidSalaryWhenFirstAttemptSucceeds() {
        // Given: Mock responses for employee details
        Long employeeId = 5L;
        Employee mockEmployee = podamFactory.manufacturePojo(Employee.class);
        mockEmployee.setEmployeeId(employeeId);
        
        Salary mockSalary = Salary.builder()
                .id(500L)
                .amount(BigDecimal.valueOf(80000))
                .currency("EUR")
                .build();
                
        DepartmentInfo mockDepartment = DepartmentInfo.builder()
                .id(3L)
                .name("Finance")
                .build();

        when(employeeService.findEmployeeById(employeeId)).thenReturn(mockEmployee);
        when(salaryService.calculateSalary(employeeId)).thenReturn(mockSalary);
        when(departmentService.getDepartmentByEmployeeId(employeeId)).thenReturn(mockDepartment);

        // When: Getting employee with first valid salary
        Employee result = structuredConcurrencyService.getEmployeeWithFirstValidSalary(employeeId);

        // Then: Should return employee with calculated details
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo(employeeId);
        assertThat(result.getSalary()).isNotNull();
        assertThat(result.getDepartmentInfo()).isNotNull();
    }

    @Test
    @DisplayName("should handle interruption during concurrent operations")
    void shouldHandleInterruptionDuringConcurrentOperations() {
        // Given: Current thread is interrupted
        Long employeeId = 1L;
        Thread.currentThread().interrupt();

        // When & Then: Should throw RuntimeException due to interruption
        assertThatThrownBy(() -> structuredConcurrencyService.getEmployeeDetailsWithConcurrency(employeeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Employee fetch interrupted");

        // Clean up interrupt status
        Thread.interrupted();
    }

    @Test
    @DisplayName("should handle concurrent calculation with different results")
    void shouldHandleConcurrentCalculationWithDifferentResults() {
        // Given: Different mock responses that would produce different calculation results
        Long employeeId = 7L;
        Employee mockEmployee = podamFactory.manufacturePojo(Employee.class);
        mockEmployee.setEmployeeId(employeeId);

        Salary baseSalary = Salary.builder()
                .id(700L)
                .amount(BigDecimal.valueOf(70000))
                .currency("USD")
                .build();

        DepartmentInfo mockDepartment = DepartmentInfo.builder()
                .id(1L)
                .name("Engineering")
                .build();

        when(employeeService.findEmployeeById(employeeId)).thenReturn(mockEmployee);
        when(salaryService.calculateSalary(employeeId)).thenReturn(baseSalary);
        when(departmentService.getDepartmentByEmployeeId(employeeId)).thenReturn(mockDepartment);

        // When: Getting employee with first valid salary (first successful wins)
        Employee result = structuredConcurrencyService.getEmployeeWithFirstValidSalary(employeeId);

        // Then: Should return first successful result
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo(employeeId);
        assertThat(result.getSalary()).isNotNull();
        assertThat(result.getDepartmentInfo()).isNotNull();
    }

    private Employee createTestEmployee(Long employeeId) {
        Employee employee = podamFactory.manufacturePojo(Employee.class);
        employee.setEmployeeId(employeeId);
        employee.setSalary(Salary.builder()
                .id(employeeId * 100)
                .amount(BigDecimal.valueOf(50000 + employeeId * 1000))
                .currency("USD")
                .build());
        employee.setDepartmentInfo(DepartmentInfo.builder()
                .id(employeeId)
                .name("Test Department " + employeeId)
                .build());
        return employee;
    }
}