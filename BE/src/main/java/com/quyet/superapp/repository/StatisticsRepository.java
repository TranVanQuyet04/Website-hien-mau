package com.quyet.superapp.repository;

import com.quyet.superapp.dto.DonorDetailDTO;
import com.quyet.superapp.dto.ReadinessStackedDTO;
import com.quyet.superapp.dto.StatisticFilterDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface StatisticsRepository {

    long countDistinctDonorsByFilter(StatisticFilterDTO filter);

    Map<String, Integer> countByBloodGroup(StatisticFilterDTO filter);

    Map<String, Double> calculateComponentRatios(StatisticFilterDTO filter);

    List<ReadinessStackedDTO> getStackedReadinessByBloodGroup(StatisticFilterDTO filter);

    List<DonorDetailDTO> getFilteredDonorDetails(StatisticFilterDTO filter);

}
