package com.example.clarityexercise.services;

import com.example.clarityexercise.requests.MetricRequest;
import com.example.clarityexercise.responses.MetricResponse;
import com.example.clarityexercise.responses.MetricSummaryResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the metric service
 */
public interface MetricService {
    /**
     * Gets the metric by its id
     *
     * @param id the id of the metric you want to retrieve
     * @return Optional of MetricResponse
     */
    Optional<MetricResponse> getMetricById(Long id);

    /**
     * Get all the metrics in the database
     *
     * @return List of all the metrics
     */
    List<MetricResponse> getAllMetrics();

    /**
     * Get Metrics with parameters
     *
     * @param system the system you want to filter on
     * @param name   the name of the metric
     * @param from   time you wish to search from
     * @param to     time you wish to search up to
     * @return list of metrics meeting the criteria
     */
    List<MetricResponse> getMetricsBySystemNameDateRange(String system, String name, Long from, Long to);

    /**
     * Get a summary of the metrics - this is sum of the values between the dates grouped by the metric name and system
     *
     * @param system system you wish to filter on
     * @param name   metric you wish to filter on
     * @param from   time you wish to search from
     * @param to     time you wish to search to
     * @return list of metric summary(s)
     */
    List<MetricSummaryResponse> getMetricSummary(String system, String name, Long from, Long to);

    /**
     * Create metric
     *
     * @param metricRequest to create
     * @return metric response of the newly created metric
     */
    Optional<MetricResponse> createMetric(MetricRequest metricRequest);

    /**
     * Update metric
     *
     * @param metricRequest metric to update
     * @return metricResponse post update
     */
    Optional<MetricResponse> updateMetric(MetricRequest metricRequest);
}
