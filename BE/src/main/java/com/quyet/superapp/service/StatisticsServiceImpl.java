package com.quyet.superapp.service;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;
    private final DonationRepository donationRepository;
    private final UrgentDonorRegistryRepository registryRepo;
    private final BloodTypeRepository bloodTypeRepository;
    private final BloodRequestRepository requestRepo;

    // Không cần khai báo thêm map ở đây nếu không dùng
    // private Map<String, Integer> rejectionReasons;
    // private Map<String, Integer> recoveryTimeChart;

    @Override
    public StatisticSummaryDTO getSummary(StatisticFilterDTO filter) {
        Long totalDonors = userRepository.count(); // Có thể count theo role nếu cần
        Long totalDonations = donationRepository.count();
        LocalDateTime lastDonation = donationRepository.findLastDonationDate();

        String mostCommonBloodType = bloodTypeRepository.findMostCommonBloodType();

        return StatisticSummaryDTO.builder()
                .totalDonors(totalDonors)
                .totalDonations(totalDonations)
                .lastDonation(lastDonation)
                .mostCommonBloodType(mostCommonBloodType)
                .build();
    }

    @Override
    public SummaryKPIDTO getSummaryKPI(StatisticFilterDTO filter) {
        LocalDateTime start = filter.getFromDate().atStartOfDay();
        LocalDateTime end = filter.getToDate().atTime(LocalTime.MAX);

        Double avgRecovery = donationRepository.calculateAverageRecoveryTime(start, end);
        int avgRecoveryDays = (avgRecovery != null) ? avgRecovery.intValue() : 0;

        return SummaryKPIDTO.builder()
                .totalDonors(userRepository.countDistinctDonorsByFilter(
                        start,
                        end,
                        filter.getBloodTypeId()
                ))
                .emergencyDonations(donationRepository.countEmergencyDonations(start, end))
                .regularDonations(donationRepository.countRegularDonations(start, end))
                .rejectionRate(donationRepository.calculateRejectionRate(start, end))
                .averageRecoveryDays(avgRecoveryDays)
                .totalSuccessfulTransfusions((int) requestRepo.countSuccessfulRequests(start, end))
                .hospitalCount(1)
                .build();
    }

    @Override
    public ChartDataDTO getChartData(StatisticFilterDTO filter) {
        LocalDateTime start = filter.getFromDate().atStartOfDay();
        LocalDateTime end = filter.getToDate().atTime(LocalTime.MAX);

        Map<String, Integer> bloodGroupCounts = convertToMap(donationRepository.countByBloodGroup(start, end));
        Map<String, Integer> componentCounts = convertToMap(donationRepository.countByComponent(start, end));
        Map<String, Integer> rejectionReasons = convertToMap(donationRepository.countRejectionReasons(start, end));
        Map<String, Integer> recoveryTimeChart = convertToMap(donationRepository.countRecoveryDaysDistributionNative(start, end));
        Map<String, Double> componentRatios = componentCounts.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().doubleValue()
                ));

        return ChartDataDTO.builder()
                .bloodGroupCounts(bloodGroupCounts)
                .componentRatios(componentRatios)
                .rejectionReasons(rejectionReasons)
                .recoveryTimeChart(recoveryTimeChart)
                .build();
    }

    private Map<String, Integer> convertToMap(List<Object[]> rawList) {
        return rawList.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),                 // ✅ An toàn: chuyển bất kỳ kiểu nào sang String
                        row -> ((Number) row[1]).intValue()       // ✅ An toàn hơn Long cast cứng
                ));
    }


    @Override
    public List<DonorDetailDTO> getDonorDetails(StatisticFilterDTO filter) {
        return registryRepo.getFilteredDonorDetails(
                filter.getFromDate().atStartOfDay(),
                filter.getToDate().atTime(LocalTime.MAX),
                filter.getBloodTypeId(),
                filter.getComponentId(),
                filter.getReadinessLevel()
        );


    }
}
