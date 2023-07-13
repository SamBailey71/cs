package com.example.clarityexercise.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
/**
 * Metric request object
 */
public class MetricRequest {
    private String system;
    private String name;
    private Long date;
    private Long value;

}
