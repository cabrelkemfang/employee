package com.example.employee.api;

import com.example.employee.domain.dto.EmployeeRequest;
import com.example.employee.domain.dto.EmployeeResponse;
import com.example.employee.domain.dto.PaginatedResponse;
import com.example.employee.domain.service.EmployeeService;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponse>> findEmployee(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        var employee = employeeService.getEmployees(page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(getHttpHeaders(employee))
                .contentType(MediaType.APPLICATION_JSON)
                .body(employee.response());
    }

    @GetMapping(value = "/{employee_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> findEmployeeById(@PathVariable("employee_id") Long employeeId) {
        var employee = employeeService.findEmployeeById(employeeId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(employee);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        employeeService.createEmployee(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Employee created Successfully");
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
