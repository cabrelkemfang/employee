package com.example.employee.config;

import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SpringBootTestLoader {

//    @LocalServerPort
//    private Integer port;
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.port = port;
//    }
}
