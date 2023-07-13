package com.example.clarityexercise.repositories;

import com.example.clarityexercise.entities.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPARepository for Metric
 */
@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    /**
     * Return a metric by parameters
     *
     * @param system chosen system
     * @param name   chosen name
     * @param date   date of metric
     * @return metric entity
     */
    Metric findMetricBySystemAndAndNameAndDate(String system, String name, Long date);

    /**
     * Return a list of metrics for a given system
     *
     * @param system chosen system
     * @return list of metric entities
     */
    List<Metric> findMetricsBySystem(String system);
}