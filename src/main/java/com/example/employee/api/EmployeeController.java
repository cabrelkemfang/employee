package com.example.employee.api;

import com.example.employee.api.dto.EmployeeRequest;
import com.example.employee.api.dto.EmployeeResponse;
import com.example.employee.api.mapper.EmployeeResponseMapper;
import com.example.employee.domain.dto.PaginatedResponse;
import com.example.employee.domain.service.decoratorPatten.EmployeeServiceDecorator;
import com.example.employee.domain.service.payment.EmployeePaymentService;
import com.example.employee.domain.service.repository.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeePaymentService employeePaymentService;
    private final EmployeeServiceDecorator employeeServiceDecorator;
    private final EmployeeResponseMapper employeeResponseMapper;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponse>> findEmployee(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        var employee = employeeService.getEmployees(page, size);
        var paginatedResponse = employeeResponseMapper.mapToPaginatedResponse(employee);

        return ResponseEntity.ok()
                .headers(getHttpHeaders(paginatedResponse))
                .contentType(MediaType.APPLICATION_JSON)
                .body(paginatedResponse.response());
    }

    @GetMapping(value = "/{employee_id}/make_payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> makePayment(@PathVariable("employee_id") Long employeeId) {
        employeePaymentService.makePayment(employeeId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Employee payment done successfully");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        var employee = employeeResponseMapper.mapToEmployee(employeeRequest);
        employeeServiceDecorator.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Employee created Successfully");
    }

    @GetMapping(value = "/{employee_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> findEmployeeById(@PathVariable("employee_id") Long employeeId) {
        var employee = employeeService.findEmployeeById(employeeId);
        var employeeResponse = employeeResponseMapper.mapToEmployeeResponse(employee);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(employeeResponse);
    }

    private static HttpHeaders getHttpHeaders(PaginatedResponse<EmployeeResponse> employee) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(employee.totalElements()));
        headers.add("X-Total-Pages", String.valueOf(employee.totalPages()));
        headers.add("X-Current-Page", String.valueOf(employee.currentPage()));
        headers.add("X-Page-Size", String.valueOf(employee.pageSize()));
        return headers;
    }
}
