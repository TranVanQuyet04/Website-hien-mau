package com.quyet.superapp.service;

import com.quyet.superapp.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface StatisticsService {
    SummaryKPIDTO getSummaryKPI(StatisticFilterDTO filter);
    ChartDataDTO getChartData(StatisticFilterDTO filter);
    List<DonorDetailDTO> getDonorDetails(StatisticFilterDTO filter);
    StatisticSummaryDTO getSummary(StatisticFilterDTO filter);

}
