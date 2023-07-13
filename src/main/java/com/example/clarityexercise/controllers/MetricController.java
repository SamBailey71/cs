package com.example.clarityexercise.controllers;

import com.example.clarityexercise.exceptions.InvalidParameterException;
import com.example.clarityexercise.exceptions.MetricNotFoundException;
import com.example.clarityexercise.requests.MetricRequest;
import com.example.clarityexercise.responses.MetricResponse;
import com.example.clarityexercise.responses.MetricSummaryResponse;
import com.example.clarityexercise.services.MetricService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Controller class for Metric and Metric summary endpoints
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/metrics")
public class MetricController {
    private final MetricService metricService;

    /**
     * ctor
     *
     * @param metricService MetricService to be injected
     */
    MetricController(final MetricService metricService) {
        this.metricService = metricService;
    }

    /**
     * Get a metric by id
     *
     * @param id the id of the metric you want
     * @return responseEntity with the metric response
     * @throws MetricNotFoundException
     */
    @GetMapping(value = "/byId/{id}")
    ResponseEntity<MetricResponse> getMetricById(final @PathVariable Long id) throws MetricNotFoundException {
        log.info("Retrieving metric id :" + id);
        final var metricResponse = metricService.getMetricById(id);
        log.info("Retrieved metric for id :" + id + " = " + metricResponse.toString());
        return metricResponse
                .map(mr -> {
                    try {
                        getLinks(id).forEach(link -> mr.add(link));
                    } catch (MetricNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return new ResponseEntity<>(mr, HttpStatus.OK);
                })
                .orElseThrow(MetricNotFoundException::new);
    }

    /**
     * Post a new metric
     *
     * @param metricRequest the new metric you want to create
     * @return response entity with the new metric as a metric response
     * @throws InvalidParameterException
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MetricResponse> createMetric(final @RequestBody MetricRequest metricRequest) throws InvalidParameterException {
        log.info("Creating Metric :" + metricRequest);
        if (metricRequest.getDate() == null || metricRequest.getDate() == 0) {
            metricRequest.setDate(Instant.now().getEpochSecond());
        }
        if (metricRequest.getValue() == null) {
            metricRequest.setValue(1L);
        }
        final var metricResponse = metricService.createMetric(metricRequest);
        log.info("Created metric :" + metricResponse.toString());
        return metricResponse
                .map(mr -> {
                    try {
                        getLinks(mr.getId()).forEach(link -> mr.add(link));
                    } catch (MetricNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return new ResponseEntity<>(mr, HttpStatus.OK);
                })
                .orElseThrow(InvalidParameterException::new);
    }

    /**
     * Put an existing metric
     *
     * @param metricRequest the metric you want to update
     * @return response entity with the updated metric and new value
     * @throws InvalidParameterException
     * @throws MetricNotFoundException
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MetricResponse> updateMetric(final @RequestBody MetricRequest metricRequest) throws InvalidParameterException, MetricNotFoundException {
        log.info("Updating metric :" + metricRequest.toString());
        if (metricRequest.getSystem() == null || metricRequest.getName() == null || metricRequest.getDate() == null) {
            throw new InvalidParameterException();
        }
        final var metricResponse = metricService.updateMetric(metricRequest);
        log.info("Metric updated :" + metricResponse.toString());
        return metricResponse
                .map(mr -> {
                            try {
                                getLinks(mr.getId()).forEach(link -> mr.add(link));
                            } catch (MetricNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            return new ResponseEntity<>(mr, HttpStatus.OK);
                        }
                )
                .orElseThrow(MetricNotFoundException::new);
    }

    /**
     * Get all metrics
     *
     * @return list of metric responses
     */
    @GetMapping(value = "/all")
    List<MetricResponse> getAllMetrics() {
        final var retList = metricService.getAllMetrics();
        retList.forEach(mr -> {
            try {
                getLinks(mr.getId()).forEach(link -> mr.add(link));
            } catch (MetricNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("Retrieved all metrics list size :" + retList.size());
        return retList;
    }

    /**
     * Get metrics with parameters
     *
     * @param system chosen system
     * @param name   chosen metric
     * @param from   date from
     * @param to     date to
     * @return response entity with list of metric responses
     * @throws InvalidParameterException
     */
    @GetMapping
    ResponseEntity<List<MetricResponse>> getMetrics(final @RequestParam String system, final @RequestParam(required = false) String name,
                                                    final @RequestParam(required = false) Long from, final @RequestParam(required = false) Long to) throws InvalidParameterException {
        log.info("Getting list of metrics for System :" + system + "Name :" + name + " From :" + from + " To :" + to);
        if (system == null) {
            throw new InvalidParameterException();
        }
        final var retList = metricService.getMetricsBySystemNameDateRange(system, name, from, to);
        retList.forEach(mr -> {
            try {
                getLinks(mr.getId()).forEach(link -> mr.add(link));
            } catch (MetricNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("Retrieved metrics for :" + system + "Name :" + name + " From :" + from + " To :" + to + " list size :" + retList.size());
        final var metricResponses = Optional
                .ofNullable(retList);
        return metricResponses.map(mr -> new ResponseEntity<>(mr, HttpStatus.OK)).orElseThrow(InvalidParameterException::new);
    }

    /**
     * Get metric summaries with parameters
     *
     * @param system chosen system
     * @param name   chosen metric
     * @param from   date from
     * @param to     date to
     * @return response entity with a list of metric summary responses
     * @throws InvalidParameterException
     */
    @GetMapping(value = "/metricsummary")
    ResponseEntity<List<MetricSummaryResponse>> getMetricSummary(final @RequestParam String system, final @RequestParam(required = false) String name,
                                                                 final @RequestParam(required = false) Long from, final @RequestParam(required = false) Long to) throws InvalidParameterException {
        log.info("Retrieving metric summaries for system :" + system);
        if (system == null) {
            throw new InvalidParameterException();
        }
        final var retList = metricService.getMetricSummary(system, name, from, to);
        if (retList == null) {
            throw new InvalidParameterException();
        }
        final Link metricSummariesLink = linkTo(methodOn(MetricController.class).getMetricSummary(system, name, from, to)).withRel("metricSummary");
        retList.forEach(ms -> {
            ms.add(metricSummariesLink);
        });
        log.info("Retrieved metric summaries for :" + system + " list size :" + retList.size());
        final var metricSummaries = Optional.of(retList);
        return metricSummaries.map(ms -> new ResponseEntity<>(ms, HttpStatus.OK)).orElseThrow(InvalidParameterException::new);
    }

    private List<Link> getLinks(final Long id) throws MetricNotFoundException {
        final Link selfLink = linkTo(methodOn(MetricController.class).getMetricById(id)).withSelfRel();
        final Link allLink = linkTo(methodOn(MetricController.class).getAllMetrics()).withRel("allMetrics");
        return Arrays.asList(selfLink, allLink);
    }

}
