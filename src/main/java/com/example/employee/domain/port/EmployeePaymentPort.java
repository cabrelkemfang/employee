package com.example.employee.domain.port;

import com.example.employee.domain.dto.Bank;
import com.example.employee.domain.dto.Employee;

public interface EmployeePaymentPort {

    void makePayment(Employee employee);

    Bank BankPayment();
}
