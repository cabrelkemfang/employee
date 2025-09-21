package com.example.employee.domain.service.department;

import com.example.employee.domain.dto.DepartmentInfo;
import com.example.employee.domain.service.department.impl.DomainDepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for DomainDepartmentService using FIRST principles and Given-When-Then structure
 */
class DomainDepartmentServiceTest {

    private DomainDepartmentService departmentService;
    private PodamFactory podamFactory;

    @BeforeEach
    void setUp() {
        // Given: Set up the service and test data factory
        departmentService = new DomainDepartmentService();
        podamFactory = new PodamFactoryImpl();
    }

    @Test
    @DisplayName("should return engineering department when employee ID maps to department 1")
    void shouldReturnEngineeringDepartmentWhenEmployeeIdMapsToDepartment1() {
        // Given: An employee ID that maps to Engineering department (employeeId % 5 + 1 = 1)
        Long employeeId = 5L; // 5 % 5 + 1 = 1

        // When: Getting department by employee ID
        DepartmentInfo result = departmentService.getDepartmentByEmployeeId(employeeId);

        // Then: Should return Engineering department
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Engineering");
    }

    @Test
    @DisplayName("should return HR department when employee ID maps to department 2")
    void shouldReturnHRDepartmentWhenEmployeeIdMapsToDepartment2() {
        // Given: An employee ID that maps to HR department (employeeId % 5 + 1 = 2)
        Long employeeId = 1L; // 1 % 5 + 1 = 2

        // When: Getting department by employee ID
        DepartmentInfo result = departmentService.getDepartmentByEmployeeId(employeeId);

        // Then: Should return HR department
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Human Resources");
    }

    @Test
    @DisplayName("should return different departments for different employee IDs")
    void shouldReturnDifferentDepartmentsForDifferentEmployeeIds() {
        // Given: Two employee IDs that map to different departments
        Long employeeId1 = 3L; // 3 % 5 + 1 = 4 (Marketing)
        Long employeeId2 = 7L; // 7 % 5 + 1 = 3 (Finance)

        // When: Getting departments for both employees
        DepartmentInfo dept1 = departmentService.getDepartmentByEmployeeId(employeeId1);
        DepartmentInfo dept2 = departmentService.getDepartmentByEmployeeId(employeeId2);

        // Then: Should return different departments
        assertThat(dept1).isNotNull();
        assertThat(dept2).isNotNull();
        assertThat(dept1.getId()).isNotEqualTo(dept2.getId());
        assertThat(dept1.getName()).isEqualTo("Marketing");
        assertThat(dept2.getName()).isEqualTo("Finance");
    }

    @Test
    @DisplayName("should get department with timeout when timeout is sufficient")
    void shouldGetDepartmentWithTimeoutWhenTimeoutIsSufficient() {
        // Given: A valid employee ID and sufficient timeout
        Long employeeId = 2L;
        Duration timeout = Duration.ofSeconds(3);

        // When: Getting department with timeout
        DepartmentInfo result = departmentService.getDepartmentByEmployeeIdWithTimeout(employeeId, timeout);

        // Then: Should return valid department
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L); // 2 % 5 + 1 = 3
        assertThat(result.getName()).isEqualTo("Finance");
    }

    @Test
    @DisplayName("should get department by ID when department ID exists")
    void shouldGetDepartmentByIdWhenDepartmentIdExists() {
        // Given: An existing department ID
        Long departmentId = 4L;

        // When: Getting department by ID
        DepartmentInfo result = departmentService.getDepartmentById(departmentId);

        // Then: Should return the correct department
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(4L);
        assertThat(result.getName()).isEqualTo("Marketing");
    }

    @Test
    @DisplayName("should return null when department ID does not exist")
    void shouldReturnNullWhenDepartmentIdDoesNotExist() {
        // Given: A non-existing department ID
        Long nonExistingDepartmentId = 99L;

        // When: Getting department by ID
        DepartmentInfo result = departmentService.getDepartmentById(nonExistingDepartmentId);

        // Then: Should return null
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("should update department when department info is valid")
    void shouldUpdateDepartmentWhenDepartmentInfoIsValid() {
        // Given: A valid department info to update
        DepartmentInfo departmentToUpdate = podamFactory.manufacturePojo(DepartmentInfo.class);
        departmentToUpdate.setId(10L);
        departmentToUpdate.setName("Research & Development");

        // When: Updating the department
        DepartmentInfo result = departmentService.updateDepartment(departmentToUpdate);

        // Then: Should return updated department with same values
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(departmentToUpdate.getId());
        assertThat(result.getName()).isEqualTo(departmentToUpdate.getName());
    }

    @Test
    @DisplayName("should return all predefined departments correctly")
    void shouldReturnAllPredefinedDepartmentsCorrectly() {
        // Given: All predefined department IDs
        Long[] departmentIds = {1L, 2L, 3L, 4L, 5L};
        String[] expectedNames = {"Engineering", "Human Resources", "Finance", "Marketing", "Sales"};

        // When & Then: Getting all departments should return correct mappings
        for (int i = 0; i < departmentIds.length; i++) {
            DepartmentInfo department = departmentService.getDepartmentById(departmentIds[i]);
            assertThat(department).isNotNull();
            assertThat(department.getId()).isEqualTo(departmentIds[i]);
            assertThat(department.getName()).isEqualTo(expectedNames[i]);
        }
    }

    @Test
    @DisplayName("should handle interruption during department fetch")
    void shouldHandleInterruptionDuringDepartmentFetch() {
        // Given: Current thread is interrupted
        Thread.currentThread().interrupt();

        // When & Then: Should throw RuntimeException due to interruption
        assertThatThrownBy(() -> departmentService.getDepartmentByEmployeeId(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Department fetch interrupted");

        // Clean up interrupt status
        Thread.interrupted();
    }

    @Test
    @DisplayName("should handle interruption during department update")
    void shouldHandleInterruptionDuringDepartmentUpdate() {
        // Given: Current thread is interrupted and a department to update
        Thread.currentThread().interrupt();
        DepartmentInfo departmentToUpdate = DepartmentInfo.builder()
                .id(1L)
                .name("Test Department")
                .build();

        // When & Then: Should throw RuntimeException due to interruption
        assertThatThrownBy(() -> departmentService.updateDepartment(departmentToUpdate))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Department update interrupted");

        // Clean up interrupt status
        Thread.interrupted();
    }
}