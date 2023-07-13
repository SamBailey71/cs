package com.example.clarityexercise.entities;

import com.example.clarityexercise.requests.MetricRequest;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity class for a Metric
 */
@Data
@Entity(name = "Metric")
@Table(name = "\"METRIC_DATA\"")
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"metric_id\"")
    private Long id;

    @Column(name = "\"metric_system\"")
    private String system;

    @Column(name = "\"metric_name\"")
    private String name;

    @Column(name = "\"metric_date\"")
    private Long date;
    @Column(name = "\"metric_value\"")
    private Long value;

    public Metric() {

    }

    /**
     * ctor
     *
     * @param metricRequest ctor that accepts a metric request
     */
    public Metric(MetricRequest metricRequest) {
        this.system = metricRequest.getSystem();
        this.name = metricRequest.getName();
        this.date = metricRequest.getDate();
        this.value = metricRequest.getValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
