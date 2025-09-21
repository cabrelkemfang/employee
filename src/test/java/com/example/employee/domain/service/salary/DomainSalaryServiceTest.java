package com.example.employee.domain.service.salary;

import com.example.employee.domain.dto.Salary;
import com.example.employee.domain.service.salary.impl.DomainSalaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for DomainSalaryService using FIRST principles and Given-When-Then structure
 */
class DomainSalaryServiceTest {

    private DomainSalaryService salaryService;
    private PodamFactory podamFactory;

    @BeforeEach
    void setUp() {
        // Given: Set up the service and test data factory
        salaryService = new DomainSalaryService();
        podamFactory = new PodamFactoryImpl();
    }

    @Test
    @DisplayName("should calculate salary when employee ID is valid")
    void shouldCalculateSalaryWhenEmployeeIdIsValid() {
        // Given: A valid employee ID
        Long employeeId = 1L;

        // When: Calculating salary
        Salary result = salaryService.calculateSalary(employeeId);

        // Then: Should return valid salary object
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L); // employeeId * 100
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(51000)); // 50000 + (1 * 1000)
        assertThat(result.getCurrency()).isEqualTo("USD");
    }

    @Test
    @DisplayName("should calculate different salaries for different employee IDs")
    void shouldCalculateDifferentSalariesForDifferentEmployeeIds() {
        // Given: Two different employee IDs
        Long employeeId1 = 1L;
        Long employeeId2 = 5L;

        // When: Calculating salaries for both employees
        Salary salary1 = salaryService.calculateSalary(employeeId1);
        Salary salary2 = salaryService.calculateSalary(employeeId2);

        // Then: Should return different salary amounts
        assertThat(salary1.getAmount()).isNotEqualTo(salary2.getAmount());
        assertThat(salary1.getId()).isNotEqualTo(salary2.getId());
        assertThat(salary2.getAmount()).isEqualTo(BigDecimal.valueOf(55000)); // 50000 + (5 * 1000)
    }

    @Test
    @DisplayName("should calculate salary with timeout when timeout is sufficient")
    void shouldCalculateSalaryWithTimeoutWhenTimeoutIsSufficient() {
        // Given: A valid employee ID and sufficient timeout
        Long employeeId = 3L;
        Duration timeout = Duration.ofSeconds(5);

        // When: Calculating salary with timeout
        Salary result = salaryService.calculateSalaryWithTimeout(employeeId, timeout);

        // Then: Should return valid salary object
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(300L);
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(53000));
        assertThat(result.getCurrency()).isEqualTo("USD");
    }

    @Test
    @DisplayName("should validate salary when salary is valid")
    void shouldValidateSalaryWhenSalaryIsValid() {
        // Given: A valid salary object
        Salary validSalary = Salary.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(75000))
                .currency("EUR")
                .build();

        // When: Validating the salary
        boolean result = salaryService.validateSalary(validSalary);

        // Then: Should return true
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should not validate salary when salary is null")
    void shouldNotValidateSalaryWhenSalaryIsNull() {
        // Given: A null salary
        Salary nullSalary = null;

        // When: Validating the salary
        boolean result = salaryService.validateSalary(nullSalary);

        // Then: Should return false
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should not validate salary when amount is null")
    void shouldNotValidateSalaryWhenAmountIsNull() {
        // Given: A salary with null amount
        Salary salaryWithNullAmount = Salary.builder()
                .id(1L)
                .amount(null)
                .currency("USD")
                .build();

        // When: Validating the salary
        boolean result = salaryService.validateSalary(salaryWithNullAmount);

        // Then: Should return false
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should not validate salary when amount is zero or negative")
    void shouldNotValidateSalaryWhenAmountIsZeroOrNegative() {
        // Given: Salaries with zero and negative amounts
        Salary salaryWithZeroAmount = Salary.builder()
                .id(1L)
                .amount(BigDecimal.ZERO)
                .currency("USD")
                .build();

        Salary salaryWithNegativeAmount = Salary.builder()
                .id(2L)
                .amount(BigDecimal.valueOf(-1000))
                .currency("USD")
                .build();

        // When: Validating the salaries
        boolean resultZero = salaryService.validateSalary(salaryWithZeroAmount);
        boolean resultNegative = salaryService.validateSalary(salaryWithNegativeAmount);

        // Then: Should return false for both
        assertThat(resultZero).isFalse();
        assertThat(resultNegative).isFalse();
    }

    @Test
    @DisplayName("should not validate salary when currency is null or empty")
    void shouldNotValidateSalaryWhenCurrencyIsNullOrEmpty() {
        // Given: Salaries with null and empty currency
        Salary salaryWithNullCurrency = Salary.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(50000))
                .currency(null)
                .build();

        Salary salaryWithEmptyCurrency = Salary.builder()
                .id(2L)
                .amount(BigDecimal.valueOf(50000))
                .currency("  ")
                .build();

        // When: Validating the salaries
        boolean resultNull = salaryService.validateSalary(salaryWithNullCurrency);
        boolean resultEmpty = salaryService.validateSalary(salaryWithEmptyCurrency);

        // Then: Should return false for both
        assertThat(resultNull).isFalse();
        assertThat(resultEmpty).isFalse();
    }

    @Test
    @DisplayName("should update salary when employee ID and salary are valid")
    void shouldUpdateSalaryWhenEmployeeIdAndSalaryAreValid() {
        // Given: A valid employee ID and salary
        Long employeeId = 5L;
        Salary salaryToUpdate = podamFactory.manufacturePojo(Salary.class);
        salaryToUpdate.setAmount(BigDecimal.valueOf(85000));
        salaryToUpdate.setCurrency("GBP");

        // When: Updating the salary
        Salary result = salaryService.updateSalary(employeeId, salaryToUpdate);

        // Then: Should return updated salary with same values
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(salaryToUpdate.getId());
        assertThat(result.getAmount()).isEqualTo(salaryToUpdate.getAmount());
        assertThat(result.getCurrency()).isEqualTo(salaryToUpdate.getCurrency());
    }

    @Test
    @DisplayName("should handle interruption during calculation")
    void shouldHandleInterruptionDuringCalculation() {
        // Given: Current thread is interrupted
        Thread.currentThread().interrupt();

        // When & Then: Should throw RuntimeException due to interruption
        assertThatThrownBy(() -> salaryService.calculateSalary(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Salary calculation interrupted");

        // Clean up interrupt status
        Thread.interrupted();
    }
}