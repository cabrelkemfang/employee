package com.example.employee.infra.adapter;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.dto.EmployeeResponse;
import com.example.employee.domain.dto.PaginatedResponse;
import com.example.employee.domain.exception.EmployeeNotFoundException;
import com.example.employee.domain.port.EmployeeRepositoryPort;
import com.example.employee.infra.mapper.EmployeeMapper;
import com.example.employee.infra.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EmployeeAdapter implements EmployeeRepositoryPort {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public PaginatedResponse<EmployeeResponse> getEmployees(int page, int size) {
        var employeeResponsePage = employeeRepository.findAll(PageRequest.of(page - 1, size))
                .map(employeeMapper::mapToEmployeeResponse);

        return buildResponse(employeeResponsePage);
    }

    private PaginatedResponse<EmployeeResponse> buildResponse(Page<EmployeeResponse> employeeResponsePage) {
        return PaginatedResponse.<EmployeeResponse>builder()
                .response(employeeResponsePage.getContent())
                .totalElements(employeeResponsePage.getTotalElements())
                .pageSize(employeeResponsePage.getTotalPages() + 1)
                .currentPage(employeeResponsePage.getSize())
                .build();
    }

    @Override
    public void createEmployee(EmployeeRequest employeeRequest) {
        var employee = employeeMapper.mapToEmployee(employeeRequest);
        employeeRepository.save(employee);
    }

    @Override
    public EmployeeResponse findEmployeeById(Long employeeId) {
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
}
