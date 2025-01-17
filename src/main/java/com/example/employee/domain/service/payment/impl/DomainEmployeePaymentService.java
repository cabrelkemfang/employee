package com.example.employee.domain.service.payment.impl;

import com.example.employee.domain.port.EmployeeRepositoryPort;
import com.example.employee.domain.service.payment.EmployeePaymentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DomainEmployeePaymentService implements EmployeePaymentService {

    private final PaymentFactory paymentFactory;
    private final EmployeeRepositoryPort employeeRepositoryPort;

    @Override
    public void makePayment(Long employeeId) {
        var employee = employeeRepositoryPort.findEmployeeById(employeeId);
        var employeePaymentService = paymentFactory.findPaymentService(employee.getBank());

        employeePaymentService.makePayment(employee);
    }
}
