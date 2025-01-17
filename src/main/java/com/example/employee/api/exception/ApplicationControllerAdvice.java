package com.example.employee.api.exception;

import com.example.employee.domain.exception.EmployeeAlreadyExistsException;
import com.example.employee.domain.exception.EmployeeNotFoundException;
import com.example.employee.domain.exception.InvalidEmailFormatException;
import com.example.employee.domain.exception.InvalidPhoneNumberFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;


@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApplicationControllerAdvice {

    private static final String DEFAULT_MESSAGE = "unhandled server message";

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFoundException(NoResourceFoundException ex) {
        log.warn("No Resource Found Exception: ", ex);
        var errorDetailResponse = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        errorDetailResponse.setTitle("Resource Not Found");

        return errorDetailResponse;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ProblemDetail handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        log.warn("Employee Found Exception: ", ex);
        var errorDetailResponse = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        errorDetailResponse.setType((URI.create("https://example.com/not-found")));
        errorDetailResponse.setTitle("Employee Found");

        return errorDetailResponse;
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ProblemDetail handleInvalidEmailFormatException(InvalidEmailFormatException ex) {
        log.warn("Invalid Email Format Exception: ", ex);
        var errorDetailResponse = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        errorDetailResponse.setTitle("Invalid Email Format");

        return errorDetailResponse;
    }

    @ExceptionHandler(InvalidPhoneNumberFormatException.class)
    public ProblemDetail handleInvalidPhoneNumberFormatException(InvalidPhoneNumberFormatException ex) {
        log.warn("Invalid Phone Number Format Exception: ", ex);
        var errorDetailResponse = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        errorDetailResponse.setTitle("Invalid Phone Number Format");

        return errorDetailResponse;
    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ProblemDetail handleEmployeeAlreadyExistsException(EmployeeAlreadyExistsException ex) {
        log.warn("Employee Already Exists Exception: ", ex);
        var errorDetailResponse = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        errorDetailResponse.setTitle("Employee Already  Exists");

        return errorDetailResponse;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail globalExceptionHandler(Exception ex) {
        log.warn(" Global Exception: ", ex);
        log.warn("Invalid Phone Number Format Exception: ", ex);
        var errorDetailResponse = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, DEFAULT_MESSAGE);
        errorDetailResponse.setTitle("Global Exception");

        return errorDetailResponse;
    }
}
