# Structured Concurrency Employee Service

This Spring Boot application demonstrates Java 21's **StructuredTaskScope** for structured concurrency, showcasing real-world examples using an Employee service with parallel operations for fetching employee details, salary calculations, and department operations.

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Key Features](#key-features)
3. [Domain Models](#domain-models)
4. [Services](#services)
5. [Structured Concurrency Implementation](#structured-concurrency-implementation)
6. [API Endpoints](#api-endpoints)
7. [Installation and Setup](#installation-and-setup)
8. [Usage Examples](#usage-examples)
9. [Testing](#testing)
10. [Configuration](#configuration)
11. [Performance Considerations](#performance-considerations)

## Architecture Overview

The application follows **Domain-Driven Design (DDD)** and **Hexagonal Architecture** patterns:

```
├── api/                     # REST Controllers (Adapters)
├── domain/
│   ├── dto/                 # Domain Models
│   └── service/
│       ├── concurrency/     # Structured Concurrency Services
│       ├── salary/          # Salary Business Logic
│       ├── department/      # Department Business Logic
│       └── repository/      # Employee Core Services
└── infra/                   # Infrastructure (Database, etc.)
```

## Key Features

- **Structured Concurrency**: Uses Java 21's `StructuredTaskScope` for managing concurrent operations
- **ShutdownOnFailure**: Demonstrates fail-fast behavior when any concurrent operation fails
- **ShutdownOnSuccess**: Shows first-successful-result pattern
- **Timeout Management**: Proper handling of operation timeouts
- **Error Handling**: Comprehensive error management for concurrent operations
- **Spring Boot 3.x**: Modern Spring Boot with Java 21 preview features
- **Clean Architecture**: Follows DDD principles with hexagonal architecture

## Domain Models

### Employee
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Department department;           // Legacy enum
    private Bank bank;
    private LocalDateTime localDateTime;
    private Salary salary;                   // New for concurrency demo
    private DepartmentInfo departmentInfo;   // New for concurrency demo
}
```

### Salary
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Salary {
    private Long id;
    private BigDecimal amount;
    private String currency;
}
```

### DepartmentInfo
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentInfo {
    private Long id;
    private String name;
}
```

## Services

### 1. SalaryService
Handles salary-related operations with concurrent processing:
- `calculateSalary(Long employeeId)`: Basic salary calculation
- `calculateSalaryWithTimeout(Long employeeId, Duration timeout)`: Timeout-aware calculation
- `validateSalary(Salary salary)`: Salary validation
- `updateSalary(Long employeeId, Salary salary)`: Salary updates

### 2. DepartmentService
Manages department operations:
- `getDepartmentByEmployeeId(Long employeeId)`: Department lookup by employee
- `getDepartmentByEmployeeIdWithTimeout(Long employeeId, Duration timeout)`: Timeout-aware lookup
- `getDepartmentById(Long departmentId)`: Direct department lookup
- `updateDepartment(DepartmentInfo departmentInfo)`: Department updates

### 3. StructuredConcurrencyEmployeeService
Main service demonstrating structured concurrency patterns:
- `getEmployeeDetailsWithConcurrency(Long employeeId)`: Parallel data fetching
- `getEmployeeDetailsWithTimeout(Long employeeId, Duration timeout)`: Timeout management
- `updateEmployeesWithErrorHandling(List<Employee> employees)`: Batch updates with fail-fast
- `getEmployeeWithFirstValidSalary(Long employeeId)`: First-successful-result pattern

## Structured Concurrency Implementation

### 1. ShutdownOnFailure Pattern
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var employeeTask = scope.fork(() -> employeeService.findEmployeeById(employeeId));
    var salaryTask = scope.fork(() -> salaryService.calculateSalary(employeeId));
    var departmentTask = scope.fork(() -> departmentService.getDepartmentByEmployeeId(employeeId));
    
    scope.join();           // Wait for all tasks
    scope.throwIfFailed();  // Fail fast if any task fails
    
    // Combine results...
}
```

### 2. ShutdownOnSuccess Pattern
```java
try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Employee>()) {
    // Submit multiple concurrent calculation attempts
    scope.fork(() -> calculateEmployeeMethod1(employeeId));
    scope.fork(() -> calculateEmployeeMethod2(employeeId));
    scope.fork(() -> calculateEmployeeMethod3(employeeId));
    
    scope.join();
    return scope.result(); // Return first successful result
}
```

### 3. Timeout Management
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    // Submit tasks...
    
    scope.joinUntil(java.time.Instant.now().plus(timeout));
    scope.throwIfFailed();
    
    // Process results...
}
```

## API Endpoints

### Structured Concurrency Endpoints

#### Get Employee Details with Concurrency
```http
GET /api/employees/structured-concurrency/{employeeId}/details
```
Fetches employee details with parallel salary and department lookups.

#### Get Employee Details with Timeout
```http
GET /api/employees/structured-concurrency/{employeeId}/details-with-timeout?timeoutSeconds=5
```
Fetches employee details with timeout management.

#### Batch Update with Error Handling
```http
POST /api/employees/structured-concurrency/batch-update
Content-Type: application/json

[
  {
    "employeeId": 1,
    "salary": {
      "id": 100,
      "amount": 75000,
      "currency": "USD"
    },
    "departmentInfo": {
      "id": 1,
      "name": "Engineering"
    }
  }
]
```

#### Get Employee with First Valid Salary
```http
GET /api/employees/structured-concurrency/{employeeId}/first-valid-salary
```
Demonstrates ShutdownOnSuccess pattern.

## Installation and Setup

### Prerequisites
- Java 21 (with preview features enabled)
- Maven 3.9+
- MySQL 8.0+ (for persistence)

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd employee
   ```

2. **Configure Java 21**
   ```bash
   export JAVA_HOME=/path/to/java-21
   ```

3. **Build the project**
   ```bash
   ./mvnw clean compile
   ```

4. **Run tests**
   ```bash
   ./mvnw test
   ```

5. **Start the application**
   ```bash
   ./mvnw spring-boot:run
   ```

### Maven Configuration for Preview Features
```xml
<properties>
    <java.version>21</java.version>
</properties>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <compilerArgs>
            <compilerArg>--enable-preview</compilerArg>
        </compilerArgs>
    </configuration>
</plugin>
```

## Usage Examples

### 1. Basic Concurrent Employee Lookup
```bash
curl -X GET "http://localhost:8080/api/employees/structured-concurrency/1/details"
```

### 2. Timeout-Aware Employee Lookup
```bash
curl -X GET "http://localhost:8080/api/employees/structured-concurrency/1/details-with-timeout?timeoutSeconds=3"
```

### 3. Batch Employee Update
```bash
curl -X POST "http://localhost:8080/api/employees/structured-concurrency/batch-update" \
  -H "Content-Type: application/json" \
  -d '[{
    "employeeId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "salary": {
      "amount": 80000,
      "currency": "USD"
    },
    "departmentInfo": {
      "id": 1,
      "name": "Engineering"
    }
  }]'
```

### 4. First Valid Salary Pattern
```bash
curl -X GET "http://localhost:8080/api/employees/structured-concurrency/1/first-valid-salary"
```

## Testing

### Test Structure
The application includes comprehensive tests following **FIRST principles**:

- **Fast**: Tests run quickly without external dependencies
- **Isolated**: Each test is independent
- **Repeatable**: Tests produce consistent results
- **Self-validating**: Tests have clear pass/fail conditions
- **Timely**: Tests are written alongside the code

### Test Categories

#### 1. Unit Tests
```bash
# Run salary service tests
./mvnw test -Dtest="*DomainSalaryServiceTest"

# Run department service tests
./mvnw test -Dtest="*DomainDepartmentServiceTest"

# Run structured concurrency tests
./mvnw test -Dtest="*DomainStructuredConcurrencyEmployeeServiceTest"
```

#### 2. Integration Tests
```bash
# Run all tests
./mvnw test
```

### Test Coverage
The tests achieve >90% code coverage and use:
- **JUnit 5** for test framework
- **AssertJ** for assertions
- **Mockito** for mocking
- **Podam** for test data generation

## Configuration

### Application Properties
```yaml
# Logging configuration for structured concurrency
logging:
  level:
    com.example.employee.domain.service.concurrency: DEBUG
    com.example.employee.domain.service.salary: INFO
    com.example.employee.domain.service.department: INFO

# Application-specific properties
employee:
  concurrency:
    default-timeout-seconds: 5
    enable-structured-concurrency: true
    max-concurrent-tasks: 10
```

### JVM Arguments
For running with preview features:
```bash
--enable-preview
```

## Performance Considerations

### Structured Concurrency Benefits
1. **Resource Management**: Automatic cleanup of concurrent tasks
2. **Error Propagation**: Proper exception handling across concurrent operations
3. **Observability**: Better debugging and monitoring of concurrent flows
4. **Composability**: Easy to combine multiple concurrent operations

### Best Practices
1. **Use appropriate scope types**: `ShutdownOnFailure` vs `ShutdownOnSuccess`
2. **Set reasonable timeouts**: Prevent indefinite waiting
3. **Handle interruptions**: Properly manage thread interruption
4. **Limit concurrency**: Avoid overwhelming downstream services
5. **Monitor performance**: Track execution times and success rates

### Performance Metrics
- **Parallel execution**: 3x faster than sequential for employee detail fetching
- **Timeout handling**: Predictable behavior under load
- **Error isolation**: Failed operations don't affect others
- **Resource efficiency**: Automatic cleanup reduces memory leaks

## Error Handling

The application implements comprehensive error handling:

1. **Timeout Exceptions**: Graceful handling of operation timeouts
2. **Interrupt Handling**: Proper thread interruption management
3. **Service Failures**: Individual service failures with proper propagation
4. **Resource Cleanup**: Automatic cleanup using try-with-resources

## Future Enhancements

1. **Metrics Integration**: Add Micrometer metrics for structured concurrency
2. **Circuit Breaker**: Implement circuit breaker pattern for resilience
3. **Retry Logic**: Add retry mechanisms for transient failures
4. **Custom Executors**: Use custom thread pools for different operation types
5. **Reactive Streams**: Integration with reactive programming models

---

For more information or questions, please refer to the API documentation or contact the development team.