//package com.example.employee.controller;
//
//import com.example.employee.EmployeeApplication;
//import io.restassured.RestAssured;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {EmployeeApplication.class})
//@RequiredArgsConstructor
//@ActiveProfiles("test")
//public class BaseTest {
//
//    @LocalServerPort
//    private Integer port;
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.port = port;
//    }
//}
