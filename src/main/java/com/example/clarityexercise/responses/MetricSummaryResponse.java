package com.example.clarityexercise.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@ToString
/**
 * Metric summary object
 */
public class MetricSummaryResponse extends RepresentationModel<MetricSummaryResponse> {
    private String system;
    private String name;
    private Long from;
    private Long to;
    private Long value;
}
