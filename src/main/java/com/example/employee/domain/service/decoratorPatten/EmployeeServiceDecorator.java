package com.example.employee.domain.service.decoratorPatten;

import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.dto.PaginatedResponse;
import com.example.employee.domain.service.repository.EmployeeService;

public abstract class EmployeeServiceDecorator implements EmployeeService {

    protected final EmployeeService employeeService;

    protected EmployeeServiceDecorator(EmployeeService decoratedEmployeeService) {
        this.employeeService = decoratedEmployeeService;
    }

    @Override
    public PaginatedResponse<Employee> getEmployees(int page, int size) {
        return employeeService.getEmployees(page, size);
    }

    @Override
    public Employee findEmployeeById(Long employeeId) {
        return employeeService.findEmployeeById(employeeId);
    }

    @Override
    public void createEmployee(Employee employee) {
        employeeService.createEmployee(employee);
    }

}
