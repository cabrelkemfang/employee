package com.example.employee.infra.payment.absaAdapter.service;

import com.example.employee.domain.dto.Bank;
import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.port.EmployeePaymentPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("ABSA")
@Slf4j
public class AbsaPayment implements EmployeePaymentPort {

    @Override
    public void makePayment(Employee employee) {
        // logic to make ABSA payment
        log.info("ABSA Payment for for employee {} , bank {}", employee.getFirstName(), employee
                .getBank());
    }

    @Override
    public Bank BankPayment() {
        return Bank.ABSA;
    }
}
