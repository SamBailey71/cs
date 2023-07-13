package com.example.clarityexercise.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private static final String SQL_CONSTRAINT_VIOLATION = "Your input has violated a constraint, your input is not unique";
    private static final String METRIC_NOT_FOUND_MESSAGE = "The specified metric was not found";
    private static final String INVALID_PARAM_MESSAGE = "A required parameter was not supplied or is invalid";
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setup() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleMetricNotFound() {
        final var exception = new MetricNotFoundException();
        final var actual = globalExceptionHandler.handleMetricNotFound(exception);
        assertEquals(METRIC_NOT_FOUND_MESSAGE, actual.getBody().getMessage());
    }

    @Test
    void handleInvalidParameterException() {
        final var exception = new InvalidParameterException();
        final var actual = globalExceptionHandler.handleInvalidParameterException(exception);
        assertEquals(INVALID_PARAM_MESSAGE, actual.getBody().getMessage());
    }

    @Test
    void handleConstraintViolationException() {
        final var exception = new SQLIntegrityConstraintViolationException();
        final var actual = globalExceptionHandler.handleConstraintViolationException(exception);
        assertEquals(SQL_CONSTRAINT_VIOLATION, actual.getBody().getMessage());
    }

    @Test
    void handleArgumentTypeMismatchException() {
        final var exception = new MethodArgumentTypeMismatchException(mock(Object.class),
                Object.class, "TestObject", mock(MethodParameter.class), mock(Throwable.class));
        final var actual = globalExceptionHandler.handleArgumentTypeMismatchException(exception);
        assertEquals(INVALID_PARAM_MESSAGE, actual.getBody().getMessage());
    }

    @Test
    void handleNoSuchElementException() {
        final var exception = new NoSuchElementException();
        final var actual = globalExceptionHandler.handleNoSuchElementException(exception);
        assertEquals(METRIC_NOT_FOUND_MESSAGE, actual.getBody().getMessage());
    }
}