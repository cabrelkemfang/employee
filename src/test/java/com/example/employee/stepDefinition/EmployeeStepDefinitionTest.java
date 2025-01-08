package com.example.employee.stepDefinition;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.dto.EmployeeResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeStepDefinitionTest {

    private Response response;
    private EmployeeRequest employeeRequest;
    private String employeeId;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
    @Given("The user is on the employee creation page and wants to create an employee with the following details:")
    public void theUserIsOnTheEmployeeCreationPageAndWantsToCreateAnEmployeeWithTheFollowingDetails(EmployeeRequest employee) {
        employeeRequest = employee;
    }

    @When("The user submits the request to create the employee")
    public void theUserSubmitsTheRequestToCreateTheEmployee() {
        response = given()
                .contentType(ContentType.JSON)
                .body(employeeRequest)
                .post("/api/employees");
    }

    @Then("The employee should be successfully created with the message: {string}")
    public void theEmployeeShouldBeSuccessfullyCreatedWithTheMessage(String message) {
        response.then().statusCode(201);
        var employeeResponse = response.asString();
        assertThat(employeeResponse).isEqualTo(message);
    }

    @Given("The user is on the employee details page and requests the employee details for employee ID {string}")
    public void theUserIsOnTheEmployeeDetailsPageAndRequestsTheEmployeeDetailsForEmployeeID(String id) {
        employeeId = id;
    }

    @When("The user submits the request to get the employee details")
    public void theUserSubmitsTheRequestToGetTheEmployeeDetails() {
        response = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/employees/{employee_id}", employeeId);
    }

    @Then("The employee details should be returned with the following information:")
    public void theEmployeeDetailsShouldBeReturnedWithTheFollowingInformation(EmployeeResponse employee) {
        var employeeResponse = response.as(EmployeeResponse.class);

        assertThat(employeeResponse).usingRecursiveComparison()
                .ignoringFields("localDateTime")
                .isEqualTo(employee);
    }

    @When("The user requests the list of all employees")
    public void theUserRequestsTheListOfAllEmployees() {
        response = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/employees");
    }

    @Then("The list of employees should be returned the following :")
    public void theListOfEmployeesShouldBeReturnedTheFollowing(List<EmployeeResponse> employees) {
        var employeeResponses = response.as(new TypeRef<List<EmployeeResponse>>() {
        });

        assertThat(employeeResponses)
                .extracting(EmployeeResponse::localDateTime)
                .isNotNull();

        assertThat(employeeResponses)
                .extracting(EmployeeResponse::employeeId)
                .isNotNull();

        assertThat(employeeResponses)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("localDateTime")
                .isEqualTo(employees);
    }

    @Then("The request should fail with the following message :{string}")
    public void theRequestShouldFailWithTheFollowingMessage(String message) {
        response.then().statusCode(400);
        String actualMessage = response.jsonPath().getString("detail");
        assertThat(actualMessage).isEqualTo(message);
    }
}