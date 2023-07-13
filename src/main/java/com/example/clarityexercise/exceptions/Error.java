package com.example.clarityexercise.exceptions;

import lombok.Data;

@Data
/**
 * Error data class
 */
public class Error {
    private String message;
    private Long timeStamp;
}
