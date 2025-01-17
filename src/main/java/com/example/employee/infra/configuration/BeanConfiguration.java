package com.example.employee.infra.configuration;

import com.example.employee.domain.port.EmployeePaymentPort;
import com.example.employee.domain.port.EmployeeRepositoryPort;
import com.example.employee.domain.service.decoratorPatten.EmployeeRequestValidation;
import com.example.employee.domain.service.decoratorPatten.EmployeeServiceValidationDecorator;
import com.example.employee.domain.service.payment.EmployeePaymentService;
import com.example.employee.domain.service.payment.impl.DomainEmployeePaymentService;
import com.example.employee.domain.service.payment.impl.PaymentFactory;
import com.example.employee.domain.service.repository.EmployeeService;
import com.example.employee.domain.service.repository.impl.DomainEmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class BeanConfiguration {

    @Bean
    public EmployeeService employeeService(EmployeeRepositoryPort employeeRepositoryPort) {
        return new DomainEmployeeService(employeeRepositoryPort);
    }

    @Bean
    public EmployeePaymentService employeePaymentService(EmployeeRepositoryPort employeeRepositoryPort,
                                                         PaymentFactory paymentFactory) {
        return new DomainEmployeePaymentService(paymentFactory, employeeRepositoryPort);
    }

    @Bean
    public PaymentFactory paymentFactory(Map<String, EmployeePaymentPort> employeePaymentPortMap,
                                         List<EmployeePaymentPort> employeePaymentPorts) {
        return new PaymentFactory(employeePaymentPortMap, employeePaymentPorts);
    }

    @Bean
    public EmployeeServiceValidationDecorator employeeServiceValidationDecorator(EmployeeService employeeService,
                                                                                 EmployeeRequestValidation employeeRequestValidation) {
        return new EmployeeServiceValidationDecorator(employeeService, employeeRequestValidation);
    }

    @Bean
    public EmployeeRequestValidation employeeRequestValidation(EmployeeRepositoryPort employeeRepositoryPort) {
        return new EmployeeRequestValidation(employeeRepositoryPort);
    }
}

