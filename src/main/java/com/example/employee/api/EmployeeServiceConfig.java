package com.example.employee.api;

import com.example.employee.domain.port.EmployeeRepositoryPort;
import com.example.employee.domain.service.EmployeeRequestValidation;
import com.example.employee.domain.service.EmployeeService;
import com.example.employee.domain.service.Impl.EmployeeServiceImpl;
import com.example.employee.domain.service.Impl.EmployeeServiceValidationDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeServiceConfig {

    @Bean
    public EmployeeServiceImpl employeeServiceImpl(EmployeeRepositoryPort employeeRepositoryPort) {
        return new EmployeeServiceImpl(employeeRepositoryPort);
    }

//    @Bean
//    public EmployeeService employeeService(EmployeeRepositoryPort employeeRepositoryPort) {
//        return new EmployeeServiceImpl(employeeRepositoryPort, new EmployeeRequestValidation());
//    }

    @Bean
    public EmployeeRequestValidation employeeRequestValidation(EmployeeRepositoryPort employeeRepositoryPort) {
        return new EmployeeRequestValidation(employeeRepositoryPort);
    }

    @Bean
    public EmployeeService employeeService(EmployeeServiceImpl employeeServiceImpl,
                                           EmployeeRequestValidation employeeRequestValidation) {
        return new EmployeeServiceValidationDecorator(employeeServiceImpl, employeeRequestValidation);
    }
}
