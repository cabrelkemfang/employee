package com.example.employee.infra.repository.mysqlAdapter.service;

import com.example.employee.domain.dto.Employee;
import com.example.employee.domain.dto.PaginatedResponse;
import com.example.employee.domain.exception.EmployeeNotFoundException;
import com.example.employee.domain.port.EmployeeRepositoryPort;
import com.example.employee.infra.repository.mysqlAdapter.mapper.EmployeeMapper;
import com.example.employee.infra.repository.mysqlAdapter.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MysqlEmployeeRepository implements EmployeeRepositoryPort {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public PaginatedResponse<Employee> getEmployees(int page, int size) {
        var employeeResponsePage = employeeRepository.findAll(PageRequest.of(page - 1, size))
                .map(employeeMapper::mapToEmployeeResponse);

        return buildResponse(employeeResponsePage);
    }

    @Override
    public void createEmployee(Employee employee) {
        var employeeEntity = employeeMapper.mapToEmployee(employee);
        employeeRepository.save(employeeEntity);
    }

    @Override
    public Employee findEmployeeById(Long employeeId) {
        var employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        return employeeMapper.mapToEmployeeResponse(employee);
    }

    @Override
    public boolean isUserPresent(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public void deleteEmployeeById(Long employeeId) {
        var employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        employeeRepository.delete(employee);
    }

    private PaginatedResponse<Employee> buildResponse(Page<Employee> employeeResponsePage) {
        return PaginatedResponse.<Employee>builder()
                .response(employeeResponsePage.getContent())
                .totalElements(employeeResponsePage.getTotalElements())
                .pageSize(employeeResponsePage.getTotalPages() + 1)
                .currentPage(employeeResponsePage.getSize())
                .build();
    }
}
