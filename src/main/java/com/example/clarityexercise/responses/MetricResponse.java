package com.example.clarityexercise.responses;

import com.example.clarityexercise.entities.Metric;
import lombok.Data;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Data
@ToString
/**
 * Metric response object
 */
public class MetricResponse extends RepresentationModel<MetricResponse> {
    private Long id;
    private String system;
    private String name;
    private Long date;
    private Long value;

    /**
     * ctor
     *
     * @param metric metric entity
     */
    public MetricResponse(final Metric metric) {
        this.id = metric.getId();
        this.system = metric.getSystem();
        this.name = metric.getName();
        this.date = metric.getDate();
        this.value = metric.getValue();
    }
}
