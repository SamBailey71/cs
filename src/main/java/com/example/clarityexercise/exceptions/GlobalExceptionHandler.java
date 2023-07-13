package com.example.clarityexercise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.NoSuchElementException;

@ControllerAdvice
/**
 * Global exception handler
 */
public class GlobalExceptionHandler {
    private static final String SQL_CONSTRAINT_VIOLATION = "Your input has violated a constraint, your input is not unique";
    private static final String METRIC_NOT_FOUND_MESSAGE = "The specified metric was not found";
    private static final String INVALID_PARAM_MESSAGE = "A required parameter was not supplied or is invalid";

    /**
     * Handler for exception not found
     *
     * @param exception the metric not found exception
     * @return response entity with the error
     */
    @ExceptionHandler(value = MetricNotFoundException.class)
    ResponseEntity<Error> handleMetricNotFound(final MetricNotFoundException exception) {
        return new ResponseEntity<Error>(getError(METRIC_NOT_FOUND_MESSAGE),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Handler for Invalid parameter exception
     *
     * @param exception the invalid parameter exception
     * @return response entity with the error
     */
    @ExceptionHandler(value = InvalidParameterException.class)
    ResponseEntity<Error> handleInvalidParameterException(final InvalidParameterException exception) {
        return new ResponseEntity<Error>(getError(INVALID_PARAM_MESSAGE),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for SQL Constraint violation, this would usually be triggered by a duplicate metric input
     *
     * @param exception the sql constraint violation exception
     * @return response entity with the error
     */
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    ResponseEntity<Error> handleConstraintViolationException(final SQLIntegrityConstraintViolationException exception) {
        return new ResponseEntity<Error>(getError(SQL_CONSTRAINT_VIOLATION),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    ResponseEntity<Error> handleArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return new ResponseEntity<Error>(getError(INVALID_PARAM_MESSAGE),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    ResponseEntity<Error> handleNoSuchElementException(final NoSuchElementException exception) {
        return new ResponseEntity<Error>(getError(METRIC_NOT_FOUND_MESSAGE), HttpStatus.NOT_FOUND);
    }

    /**
     * Gets an error object with the custom message
     *
     * @param message the message
     * @return the error object
     */
    private Error getError(final String message) {
        final var error = new Error();
        error.setMessage(message);
        error.setTimeStamp(Instant.now().getEpochSecond());
        return error;
    }
}
