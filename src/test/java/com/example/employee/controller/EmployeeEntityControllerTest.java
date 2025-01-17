package com.example.employee.controller;

import com.example.employee.EmployeeApplication;
import com.example.employee.domain.dto.Department;
import com.example.employee.domain.dto.Employee;
import com.example.employee.api.dto.EmployeeResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {EmployeeApplication.class})
@ActiveProfiles("test")
public class EmployeeEntityControllerTest {
    @LocalServerPort
    private Integer port;

    private Employee employeeRequest;
    private Response response;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        initializeEmployeeRequest();
    }

    @Test
    @Order(1)
    void ShouldCreateEmployee() {
        // when
         response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(employeeRequest)
                .post("/api/employees");

        //them
        response.then().statusCode(201);
        var employeeResponse = response.asString();
        assertThat(employeeResponse).isEqualTo("Employee created Successfully");
    }

    @Test
    @Order(2)
    void shouldFindEmployeeById() {
        // given
        var employeeId = 1L;

        //when
         response = RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/employees/{employee_id}", employeeId);

        var employeeResponse = response.as(EmployeeResponse.class);

        //then
        response.then().statusCode(200);
        assertThat(employeeResponse).usingRecursiveComparison()
                .ignoringFields("employeeId", "localDateTime")
                .isEqualTo(employeeRequest);
    }


    @Test
    @Order(3)
    void shouldFindEmployees() {
        // when
         response = RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/employees");

        var employeeResponses = response.as(new TypeRef<List<EmployeeResponse>>() {
        });

        // then
        response.then().statusCode(200);
        assertThat(employeeResponses)
                .extracting(EmployeeResponse::localDateTime)
                .isNotNull();

        assertThat(employeeResponses)
                .extracting(EmployeeResponse::employeeId)
                .isNotNull();

        assertThat(employeeResponses.getFirst())
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("employeeId", "localDateTime")
                .isEqualTo(employeeRequest);
    }

    @Test
    @Order(4)
    @DisplayName("Should fail when creating an employee with wrong email")
    void ShouldFailWhenCreateEmployee() {
        //given
        var employee = employeeRequest;
        employee.setEmail("test");

        // when
         response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(employee)
                .post("/api/employees");
        // then
        response.then().statusCode(400);
        String actualMessage = response.jsonPath().getString("detail");
        assertThat(actualMessage).isEqualTo("Invalid email format: " + employee.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Should fail when creating an employee with wrong phone number")
    void ShouldFailWhenCreateEmployeeWithWrongPhone() {
        //given
        var employee = employeeRequest;
        employee.setPhoneNumber("1234567");

        // when
         response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(employee)
                .post("/api/employees");

        // then
        response.then().statusCode(400);
        String actualMessage = response.jsonPath().getString("detail");
        assertThat(actualMessage).isEqualTo("Invalid phone number format " + employee.getPhoneNumber());
    }

    private void initializeEmployeeRequest() {
        employeeRequest = Employee.builder()
                .department(Department.ADMINISTRATION)
                .email("test@gmail.com")
                .lastName("last name")
                .firstName("first name")
                .phoneNumber("+23067363867")
                .build();
    }
}



