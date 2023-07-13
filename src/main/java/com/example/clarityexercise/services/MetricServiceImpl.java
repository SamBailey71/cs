package com.example.clarityexercise.services;

import com.example.clarityexercise.entities.Metric;
import com.example.clarityexercise.repositories.MetricRepository;
import com.example.clarityexercise.requests.MetricRequest;
import com.example.clarityexercise.responses.MetricResponse;
import com.example.clarityexercise.responses.MetricSummaryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingLong;

@Service
@Slf4j
/**
 * Concrete implementation of the MetricService
 */
public class MetricServiceImpl implements MetricService {

    private final MetricRepository metricRepository;

    /**
     * Ctor
     *
     * @param metricRepository autowired MetricRepository
     */
    MetricServiceImpl(final MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public Optional<MetricResponse> getMetricById(final Long id) {
        log.info("Getting the metric for id:" + id);
        return Optional.ofNullable(new MetricResponse(metricRepository.findById(id).get()));
    }

    @Override
    public List<MetricResponse> getAllMetrics() {
        log.info("Getting all metrics");
        return metricRepository.findAll().stream().map(m -> new MetricResponse(m)).collect(Collectors.toList());
    }


    @Override
    public List<MetricResponse> getMetricsBySystemNameDateRange(final String system, final String name, final Long from, final Long to) {
        final var systemMetrics = getMetricsForSystem(system);
        if (systemMetrics == null) {
            return null;
        }
        if (name == null && from == null && to == null) {
            return systemMetrics
                    .stream()
                    .map(m -> new MetricResponse(m))
                    .collect(Collectors.toList());
        } else if (name == null && from != null && to != null) {
            return systemMetrics.stream().filter(m -> m.getDate() >= from && m.getDate() <= to)
                    .map(m -> new MetricResponse(m)).collect(Collectors.toList());
        } else if (name != null && from == null && to == null) {
            return systemMetrics.stream().filter(m -> m.getName().equals(name))
                    .map(m -> new MetricResponse(m)).collect(Collectors.toList());
        } else if (name != null && from != null && to == null) {
            return systemMetrics.stream().filter(m -> m.getName().equals(name)).filter(m -> m.getDate() >= from)
                    .map(m -> new MetricResponse(m)).collect(Collectors.toList());
        } else if (name != null && from == null && to != null) {
            return systemMetrics.stream().filter(m -> m.getName().equals(name)).filter(m -> m.getDate() <= to)
                    .map(m -> new MetricResponse(m)).collect(Collectors.toList());
        } else if (name != null && from != null && to != null) {
            return systemMetrics.stream().filter(m -> m.getName().equals(name))
                    .filter(m -> m.getDate() >= from && m.getDate() <= to)
                    .map(m -> new MetricResponse(m)).collect(Collectors.toList());
        }
        return null;
    }


    @Override
    public List<MetricSummaryResponse> getMetricSummary(String system, String name, Long from, Long to) {
        final var systemMetrics = getMetricsForSystem(system);
        if (systemMetrics == null) {
            return null;
        }
        if (name == null && from == null && to == null) {
            final var metricsMap = systemMetrics.stream()
                    .collect(groupingBy(Metric::getName, summarizingLong(Metric::getValue)));
            return mapToSummary(metricsMap, system, name, from, to);
        } else if (name != null && from == null && to == null) {
            final var metricsMap = systemMetrics.stream().filter(m -> m.getName().equals(name))
                    .collect(groupingBy(Metric::getName, summarizingLong(Metric::getValue)));
            return mapToSummary(metricsMap, system, name, from, to);
        } else if (name != null && from != null && to == null) {
            final var metricsMap = systemMetrics.stream().filter(m -> m.getName().equals(name))
                    .filter(m -> m.getDate() >= from)
                    .collect(groupingBy(Metric::getName, summarizingLong(Metric::getValue)));
            return mapToSummary(metricsMap, system, name, from, to);
        } else if (name != null && from == null && to != null) {
            final var metricsMap = systemMetrics.stream().filter(m -> m.getName().equals(name))
                    .filter(m -> m.getDate() <= to)
                    .collect(groupingBy(Metric::getName, summarizingLong(Metric::getValue)));
            return mapToSummary(metricsMap, system, name, from, to);
        } else if (name != null && from != null && to != null) {
            final var metricsMap = systemMetrics.stream().filter(m -> m.getName().equals(name))
                    .filter(m -> m.getDate() >= from && m.getDate() <= to)
                    .collect(groupingBy(Metric::getName, summarizingLong(Metric::getValue)));
            return mapToSummary(metricsMap, system, name, from, to);
        } else if (name == null && from != null && to != null) {
            final var metricsMap = systemMetrics.stream()
                    .filter(m -> m.getDate() >= from && m.getDate() <= to)
                    .collect(groupingBy(Metric::getName, summarizingLong(Metric::getValue)));
            return mapToSummary(metricsMap, system, name, from, to);
        }
        return null;
    }

    /**
     * Takes the map etc and returns a sorted list of metric summary responses
     *
     * @param map    map of metric names and statistics
     * @param system system selected
     * @param name   name selected
     * @param from   date from
     * @param to     date to
     * @return sorted list of metric summary responses
     */
    private List<MetricSummaryResponse> mapToSummary(Map<String, LongSummaryStatistics> map,
                                                     String system, String name, Long from, Long to) {
        return map
                .keySet()
                .stream()
                .map(key -> new MetricSummaryResponse(system, key, from, to, map.get(key).getSum()))
                .sorted(Comparator.comparing(MetricSummaryResponse::getName))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<MetricResponse> createMetric(final MetricRequest metricRequest) {
        final Metric metricToCreate = new Metric(metricRequest);
        log.info("Saving Metric" + metricToCreate);
        return Optional.ofNullable(new MetricResponse(metricRepository.save(metricToCreate)));
    }

    @Override
    public Optional<MetricResponse> updateMetric(MetricRequest metricRequest) {
        final var metric = metricRepository.findMetricBySystemAndAndNameAndDate(metricRequest.getSystem(), metricRequest.getName(), metricRequest.getDate());
        if (metric == null) {
            return Optional.empty();
        }
        if (metricRequest.getValue() == 0 || metricRequest.getValue() == null) {
            metric.setValue(metric.getValue() + 1);
        } else {
            metric.setValue(metricRequest.getValue());
        }
        return Optional.ofNullable(new MetricResponse(metricRepository.save(metric)));
    }

    private List<Metric> getMetricsForSystem(String system) {
        final var metrics = metricRepository.findMetricsBySystem(system);
        if (metrics == null || metrics.isEmpty()) {
            return null;
        } else return metrics;
    }
}
