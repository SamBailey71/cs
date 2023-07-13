package com.example.clarityexercise.services;

import com.example.clarityexercise.entities.Metric;
import com.example.clarityexercise.repositories.MetricRepository;
import com.example.clarityexercise.requests.MetricRequest;
import com.example.clarityexercise.responses.MetricResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MetricServiceImplTest {

    private MetricService metricService;
    @Mock
    private MetricRepository metricRepository = mock(MetricRepository.class);
    @Mock
    private Metric metric = mock(Metric.class);
    @Mock
    private MetricRequest metricRequest = mock(MetricRequest.class);
    @Mock
    private MetricResponse metricResponse = mock(MetricResponse.class);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        metricService = new MetricServiceImpl(metricRepository);
    }

    @Test
    void getMetricById() {
        final var id = 1L;
        when(metricRepository.findById(id)).thenReturn(Optional.of(metric));
        assertEquals(new MetricResponse(metric), metricService.getMetricById(id).get());
    }

    @Test
    void getAllMetrics() {
        assertTrue(metricService.getAllMetrics() instanceof List<MetricResponse>);
    }

    @Test
    void getMetricsBySystemNameDateRange() {
        assertNull(metricService.getMetricsBySystemNameDateRange(null, null, null, null));
    }

    @Test
    void createMetric() {
        when(metricRepository.save(any())).thenReturn(metric);
        metricService.createMetric(metricRequest);
        verify(metricRepository, times(1)).save(any());
    }

    @Test
    void updateMetric() {
        final var id = 1L;
        when(metricRepository.findMetricBySystemAndAndNameAndDate(any(), any(), any())).thenReturn(metric);
        when(metricRepository.save(metric)).thenReturn(metric);
        assertEquals(new MetricResponse(metric), metricService.updateMetric(metricRequest).get());
    }
}