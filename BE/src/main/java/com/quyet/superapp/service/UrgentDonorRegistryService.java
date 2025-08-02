package com.quyet.superapp.service;

import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.mapper.NearbyDonorMapper;
import com.quyet.superapp.mapper.UrgentDonorSearchMapper;
import com.quyet.superapp.util.HospitalLocation;
import org.springframework.beans.factory.annotation.Value; // ✅ Đúng!
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.transaction.annotation.Transactional;
import com.quyet.superapp.dto.*;
import com.quyet.superapp.dto.VerifiedUrgentDonorDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.enums.DonorReadinessLevel;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.UrgentDonorListItemMapper;
import com.quyet.superapp.mapper.UrgentDonorMapper;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UrgentDonorRegistryService {

    private final UserRepository userRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final UrgentDonorRegistryRepository urgentDonorRegistryRepository;
    private final UserProfileRepository userProfileRepository;
    private final AddressService addressService;
    private final UrgentDonorMapper urgentDonorMapper;
    private final EmailService emailService;
    private final UrgentDonorRegistryRepository urgentDonorRegistryRepo;
    private final UrgentDonorListItemMapper urgentDonorListItemMapper;
    private final UrgentDonorSearchMapper mapper;
    @Autowired
    private ReadinessChangeLogRepository readinessChangeLogRepository;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UrgentDonorRegistryService.class);


    @Autowired
    private NearbyDonorMapper nearbyDonorMapper;

    @Value("${hospital.latitude}")
    private double hospitalLat;

    @Value("${hospital.longitude}")
    private double hospitalLng;


    private final DonationRepository donationRepository;


    private final UrgentDonorRegistryRepository urgentDonorRepository;

    private final UrgentDonorSearchMapper urgentDonorSearchMapper;
    private final BloodComponentRepository componentRepository;
    private final UrgentDonorRegistryRepository donorRegistryRepository;
    private final BloodTypeService bloodTypeService;
    private final DonationService donationService;
    private final BloodComponentRepository bloodComponentRepository;



    public List<UrgentDonorMatchResultDTO> searchUrgentDonors(UrgentDonorSearchRequestDTO request) {
        // Tìm nhóm máu người nhận
        BloodType receiverType = bloodTypeRepository.findById(request.getReceiverBloodTypeId())
                .orElseThrow(() -> new NoSuchElementException("Blood type not found"));

        // Tìm thành phần máu cần
        BloodComponent component = bloodComponentRepository
                .findByBloodComponentIdAndIsActiveTrue(request.getComponentId())
                .orElseThrow(() -> new RuntimeException("Component not found"));

        // Lấy tất cả người hiến máu đã xác minh có sẵn thành phần máu này
        List<UrgentDonorRegistry> allDonors =
                donorRegistryRepository.findAllVerifiedWithComponent(component.getBloodComponentId());

        List<UrgentDonorMatchResultDTO> result = new ArrayList<>();

        for (UrgentDonorRegistry reg : allDonors) {
            // Bỏ qua nếu thiếu user, profile hoặc bloodType
            if (reg.getDonor() == null || reg.getDonor().getUserProfile() == null || reg.getDonor().getProfile() == null) {
                continue;
            }

            UserProfile profile = reg.getDonor().getUserProfile();
            Double donorLat = profile.getLatitude();
            Double donorLng = profile.getLongitude();

            // Bỏ qua nếu thiếu tọa độ
            if (donorLat == null || donorLng == null) {
                continue;
            }

            BloodType donorType = reg.getDonor().getProfile().getBloodType();
            if (donorType == null) continue;

            // Kiểm tra tương thích nhóm máu
            boolean compatible = bloodTypeService.isCompatible(donorType, receiverType);

            // Tính khoảng cách tới bệnh viện
            double distance = GeoUtils.calculateDistanceKm(
                    donorLat, donorLng,
                    HospitalLocation.LATITUDE,
                    HospitalLocation.LONGITUDE
            );

            // Ngoài bán kính tìm kiếm thì bỏ qua
            if (distance > request.getRadiusKm()) continue;

            // Kiểm tra khả năng hiến thành phần máu
            boolean canDonateNow = donationService.canDonateNow(
                    reg.getDonor().getUserId(), component.getBloodComponentId());

            Integer daysLeft = donationService.getDaysUntilRecover(
                    reg.getDonor().getUserId(), component.getBloodComponentId());

            // Tạo DTO kết quả
            result.add(UrgentDonorMatchResultDTO.builder()
                    .id(reg.getId())
                    .fullName(profile.getFullName())
                    .phone(profile.getPhone())
                    .address(profile.getFullAddressString())
                    .bloodType(donorType.getDescription())
                    .component(component.getName())
                    .distance(Math.round(distance * 10.0) / 10.0)
                    .readiness(reg.getReadinessLevel().name())
                    .verified(true)
                    .compatible(compatible)
                    .canDonateNow(canDonateNow)
                    .daysUntilRecover(canDonateNow ? null : daysLeft)
                    .build());
        }

        // Sắp xếp theo khoảng cách gần nhất
        result.sort(Comparator.comparing(UrgentDonorMatchResultDTO::getDistance));

        return result;
    }






    public List<UrgentDonorSearchResultDTO> searchNearbyDonors(Long bloodTypeId, Long componentId, double maxDistanceKm) {
        List<UrgentDonorRegistry> candidates = urgentDonorRegistryRepository
                .findByBloodType_BloodTypeIdAndBloodComponent_BloodComponentIdAndIsVerifiedTrueAndIsAvailableTrue(bloodTypeId, componentId);

        double hospitalLat = HospitalLocation.LATITUDE;
        double hospitalLng = HospitalLocation.LONGITUDE;

        return candidates.stream()
                .map(donor -> {
                    double distance = GeoUtils.calculateDistanceKm(hospitalLat, hospitalLng, donor.getLatitude(), donor.getLongitude());
                    return new Object[]{donor, distance};
                })
                .filter(arr -> (double) arr[1] <= maxDistanceKm)
                .sorted(Comparator.comparingDouble(arr -> (double) arr[1])) // ✅ sort theo khoảng cách
                .map(arr -> urgentDonorSearchMapper.toDTO((UrgentDonorRegistry) arr[0], (double) arr[1]))
                .collect(Collectors.toList());
    }




    public List<NearbyDonorDTO> findNearbyVerifiedDonors(BloodRequest request, double radiusKm) {
        if (request == null || request.getBloodType() == null) {
            throw new IllegalArgumentException("BloodRequest hoặc BloodType không hợp lệ.");
        }

        Long bloodTypeId = request.getBloodType().getBloodTypeId();

        // Bệnh viện chỉ có 1 vị trí cố định
        double hospitalLat = HospitalLocation.LATITUDE;
        double hospitalLng = HospitalLocation.LONGITUDE;

        return findNearbyVerifiedDonors(hospitalLat, hospitalLng, bloodTypeId, radiusKm);
    }


    public List<NearbyDonorDTO> findNearbyVerifiedDonors(double radiusKm) {
        return findNearbyVerifiedDonors(hospitalLat, hospitalLng, null, radiusKm);
    }

    public List<NearbyDonorDTO> findNearbyVerifiedDonors(
            double lat,
            double lng,
            Long bloodTypeFilter,
            double maxDistanceKm
    ) {
        return urgentDonorRegistryRepo.findAllVerified().stream()
                .map(donor -> {
                    Double donorLat = donor.getLatitude();
                    Double donorLng = donor.getLongitude();

                    // Bỏ qua nếu thiếu tọa độ
                    if (donorLat == null || donorLng == null) return null;

                    double distance = GeoUtils.calculateDistanceKm(lat, lng, donorLat, donorLng);
                    if (distance > maxDistanceKm) return null;

                    // 👉 Lấy ID và tên nhóm máu người hiến
                    BloodType bloodType = donor.getBloodType();
                    Long donorBloodTypeId = (bloodType != null) ? bloodType.getBloodTypeId() : null;
                    String donorBloodType = (bloodType != null) ? bloodType.getDescription() : "Không rõ";

                    // Nếu lọc nhóm máu và không khớp thì bỏ qua
                    if (bloodTypeFilter != null && !bloodTypeFilter.equals(donorBloodTypeId)) {
                        return null;
                    }

                    User user = donor.getDonor();
                    UserProfile profile = user.getUserProfile();

                    String fullName = (profile != null) ? profile.getFullName() : "Không rõ";
                    String phone = (profile != null && profile.getPhone() != null) ? profile.getPhone() : "Không rõ";
                    String address = (profile != null && profile.getAddress() != null)
                            ? profile.getAddress().getAddressStreet()
                            : "Không rõ";

                    String readiness = donor.getReadinessLevel() != null
                            ? donor.getReadinessLevel().name()
                            : "UNKNOWN";

                    // 🩸 Truy xuất lần hiến gần nhất và tính toán hồi phục
                    Donation lastDonation = donationRepository.findTopByUser_UserIdOrderByDonationDateDesc(user.getUserId());

                    LocalDate lastDate = null;
                    LocalDate nextDate = null;
                    String componentName = "Không rõ";
                    boolean isEligible = false;
                    String recoveryStatus = "❓ Không rõ";

                    if (lastDonation != null && lastDonation.getBloodComponent() != null) {
                        BloodComponentType type = lastDonation.getBloodComponent().getType();
                        if (type != null && lastDonation.getDonationDate() != null) {
                            lastDate = lastDonation.getDonationDate().toLocalDate();
                            nextDate = lastDate.plusDays(type.getRecoveryDays());
                            componentName = type.getDescription();

                            if (!nextDate.isAfter(LocalDate.now())) {
                                isEligible = true;
                                recoveryStatus = "✅ Đủ điều kiện";
                            } else {
                                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), nextDate);
                                recoveryStatus = "❌ Còn " + daysLeft + " ngày mới hồi phục";
                            }
                        }
                    }

                    // ✅ Tạo DTO trả về
                    return new NearbyDonorDTO(
                            user.getUserId(),
                            fullName,
                            donorBloodType,
                            readiness,
                            BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                            address,
                            phone,
                            (lastDate != null ? lastDate : LocalDate.of(1970, 1, 1)),
                            (nextDate != null ? nextDate : LocalDate.of(1970, 1, 1)),
                            componentName,
                            isEligible,
                            recoveryStatus
                    );
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(NearbyDonorDTO::getDistanceKm))
                .collect(Collectors.toList());
    }




    public List<UrgentDonorListItemDTO> getInactiveUrgentDonors() {
        List<UrgentDonorRegistry> list = urgentDonorRegistryRepo.findUnavailableDonors();
        return list.stream()
                .map(urgentDonorListItemMapper::toDTO)
                .toList();
    }


    // Bước 5: Nếu bạn muốn hiển thị chi tiết:
// => Sử dụng UrgentDonorDetailDTO, dùng service:

    public UrgentDonorDetailDTO getDonorDetailById(Long userId) {
        var registry = urgentDonorRegistryRepository.findByDonor_UserIdAndIsVerifiedTrue(userId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        var donor = registry.getDonor();
        var profile = donor.getUserProfile();
        var address = profile != null ? profile.getAddress() : null;

        // ✅ Định dạng ngày dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String formattedDob = (profile != null && profile.getDob() != null)
                ? profile.getDob().format(formatter)
                : "--";

        String formattedLastDonationDate = (profile != null && profile.getLastDonationDate() != null)
                ? profile.getLastDonationDate().format(formatter)
                : "--";

        // ✅ Mô tả nhóm máu
        String bloodTypeDescription = (profile != null && profile.getBloodType() != null)
                ? profile.getBloodType().getDescription()
                : "--";

        // ✅ Mô tả readiness
        DonorReadinessLevel readinessLevel = registry.getReadinessLevel(); // ✅ Đây là getter Lombok tạo ra từ field `readinessLevel`

        String readinessDescription = "--";
        if (readinessLevel != null) {
            switch (readinessLevel) {
                case EMERGENCY_NOW -> readinessDescription = "HIẾN NGAY";
                case EMERGENCY_FLEXIBLE -> readinessDescription = "LINH HOẠT";
                case REGULAR -> readinessDescription = "THÔNG THƯỜNG";
                case NOT_READY -> readinessDescription = "CHƯA SẴN SÀNG";
                case UNDECIDED -> readinessDescription = "CHƯA CHỌN";
            }
        }

        return new UrgentDonorDetailDTO(
                profile != null ? profile.getFullName() : "--",
                formattedDob,
                profile != null ? profile.getGender() : "--",
                profile != null ? profile.getCitizenId() : "--",
                profile != null ? profile.getPhone() : "--",
                donor.getEmail(),
                address != null ? address.getFullAddress() : "--",
                bloodTypeDescription,
                readinessLevel,
                readinessDescription,
                formattedLastDonationDate,
                profile != null && profile.getRecoveryTime() != null
                        ? profile.getRecoveryTime() + " ngày" : "--",
                profile != null ? profile.getOccupation() : "--",
                Boolean.TRUE.equals(registry.getIsAvailable())
                        ? "✅ Đang sẵn sàng (Đã xác minh)" : "❌ Tạm dừng"
        );
    }



    public List<UrgentDonorListItemDTO> getAllVerifiedUrgentDonorsFiltered(Long bloodTypeId, String location, LocalDate minDonationDate, Integer minRecoveryTime) {
        return urgentDonorRegistryRepository.findByIsVerifiedTrueAndIsAvailableTrue().stream()
                .filter(d -> d.getReadinessLevel() == DonorReadinessLevel.EMERGENCY_NOW || d.getReadinessLevel() == DonorReadinessLevel.EMERGENCY_FLEXIBLE)
                .filter(d -> bloodTypeId == null || (d.getBloodType() != null && d.getBloodType().getBloodTypeId().equals(bloodTypeId)))
                .filter(d -> location == null || (d.getDonor().getUserProfile().getLocation() != null &&
                        d.getDonor().getUserProfile().getLocation().toLowerCase().contains(location.toLowerCase())))
                .filter(d -> minDonationDate == null || (
                        d.getDonor().getUserProfile().getLastDonationDate() != null &&
                                !d.getDonor().getUserProfile().getLastDonationDate().toLocalDate().isBefore(minDonationDate)))
                .filter(d -> minRecoveryTime == null || (
                        d.getDonor().getUserProfile().getRecoveryTime() != null &&
                                d.getDonor().getUserProfile().getRecoveryTime() >= minRecoveryTime))
                .map(urgentDonorListItemMapper::toDTO)
                .toList();
    }


    // Bước 3: Service thêm hàm lấy danh sách dạng bảng có lọc

    public List<UrgentDonorListItemDTO> getFilteredUrgentDonors(Long bloodTypeId, String location, LocalDate minDonationDate) {
        return urgentDonorRegistryRepository.findByIsVerifiedTrueAndIsAvailableTrue().stream()
                .filter(d -> d.getReadinessLevel() == DonorReadinessLevel.EMERGENCY_NOW || d.getReadinessLevel() == DonorReadinessLevel.EMERGENCY_FLEXIBLE)
                .filter(d -> bloodTypeId == null || d.getBloodType().getBloodTypeId().equals(bloodTypeId))
                .filter(d -> location == null || (d.getDonor().getUserProfile().getLocation() != null && d.getDonor().getUserProfile().getLocation().toLowerCase().contains(location.toLowerCase())))
                .filter(d -> {
                    if (minDonationDate == null) return true;
                    var date = d.getDonor().getUserProfile().getLastDonationDate();
                    return date != null && !date.toLocalDate().isBefore(minDonationDate);
                })
                .map(urgentDonorListItemMapper::toDTO)
                .toList();
    }

    public List<User> getVerifiedUrgentDonorsReady() {
        return urgentDonorRegistryRepository.findVerifiedUrgentDonorsReady();
    }

    public List<UrgentDonorListItemDTO> getAllVerifiedUrgentDonorsForTable() {
        return urgentDonorRegistryRepository.findByIsVerifiedTrueAndIsAvailableTrue()
                .stream()
                .filter(d -> {
                    DonorReadinessLevel mode = d.getReadinessLevel();
                    return mode == DonorReadinessLevel.EMERGENCY_NOW || mode == DonorReadinessLevel.EMERGENCY_FLEXIBLE;
                })
                .map(urgentDonorListItemMapper::toDTO)
                .toList();
    }


    // Đăng ký đơn giản theo readiness level
    public void register(Long userId, DonorReadinessLevel level, UrgentDonorRegistrationDTO dto) {
        // ✅ 1. Lấy người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));

        // ✅ 2. Lấy hoặc kiểm tra hồ sơ cá nhân
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new IllegalStateException("Người dùng chưa có hồ sơ cá nhân. Không thể đăng ký hiến máu khẩn cấp.");
        }

        // ✅ 3. Cập nhật tọa độ nếu chưa có
        if (dto.getLatitude() != null) profile.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) profile.setLongitude(dto.getLongitude());

        // ✅ 4. Cập nhật nhóm máu nếu chưa có
        if (profile.getBloodType() == null && dto.getBloodTypeId() != null) {
            BloodType bloodType = bloodTypeRepository.findById(dto.getBloodTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhóm máu với ID: " + dto.getBloodTypeId()));
            profile.setBloodType(bloodType); // ✅ SỬA Ở ĐÂY
        }

        userProfileRepository.save(profile); // ✅ Lưu lại profile sau khi cập nhật

        // ✅ 5. Tạo hoặc cập nhật registry
        UrgentDonorRegistry registry = urgentDonorRegistryRepo.findByDonor(user).orElse(null);
        if (registry == null) {
            registry = new UrgentDonorRegistry();
            registry.setDonor(user);
            registry.setRegisteredAt(LocalDateTime.now());
        }

        // ✅ 6. Gán dữ liệu đầy đủ cho registry
        registry.setBloodType(profile.getBloodType()); // ✅ chính xác kiểu entity
        registry.setReadinessLevel(level);
        registry.setLatitude(dto.getLatitude());
        registry.setLongitude(dto.getLongitude());
        registry.setIsAvailable(true);
        registry.setIsVerified(false);
        registry.setLeftGroupAt(null);

        // ✅ 7. Lưu registry
        urgentDonorRegistryRepo.save(registry);
    }




    // Đăng ký đầy đủ (có vị trí và địa chỉ)
    public void registerOrUpdateUrgentDonor(Long userId, UrgentDonorRegistrationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        UrgentDonorRegistry registry = urgentDonorRegistryRepo.findByDonor(user).orElse(null);
        BloodType bloodType = bloodTypeRepository.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhóm máu"));


        if (registry == null) {
            registry = new UrgentDonorRegistry();
            registry.setDonor(user);
            registry.setRegisteredAt(LocalDateTime.now());
        }

        registry.setBloodType(bloodType);
        registry.setReadinessLevel(DonorReadinessLevel.EMERGENCY_NOW); // Hoặc dto.getReadinessLevel() nếu có
        registry.setLatitude(dto.getLatitude());
        registry.setLongitude(dto.getLongitude());
        registry.setIsAvailable(true);
        registry.setIsVerified(false);
        registry.setLeftGroupAt(null);
        registry.setRegisteredAt(LocalDateTime.now());

        urgentDonorRegistryRepo.save(registry);
    }




    public List<UrgentDonorRegistry> getAllAvailableDonors() {
        return urgentDonorRegistryRepository.findAvailableDonorsAll();
    }

    public List<UrgentDonorRegistry> findNearbyDonors(double lat, double lng, double radiusKm) {
        return urgentDonorRegistryRepository.findNearbyVerifiedDonors(lat, lng, radiusKm);
    }

    public List<UrgentDonorResponseDTO> filterDonorsByBloodTypeAndDistance(Long bloodTypeId, double lat, double lng, double radiusKm) {
        return urgentDonorRegistryRepository.findNearbyVerifiedDonors(lat, lng, radiusKm)
                .stream()
                .filter(d -> d.getBloodType().getBloodTypeId().equals(bloodTypeId))
                .map(d -> {
                    UserProfile profile = d.getDonor().getUserProfile();
                    return new UrgentDonorResponseDTO(
                            d.getDonor().getUserId(),
                            profile.getFullName(),
                            d.getBloodType().getDescription(),
                            profile.getLocation(),
                            profile.getPhone(),
                            profile.getAddress()
                    );
                })
                .toList();
    }

    public List<UnverifiedDonorDTO> getUnverifiedDonors() {
        return urgentDonorRegistryRepository.findUnverifiedDonors()
                .stream()
                .map(urgentDonorMapper::toUnverifiedDTO)
                .toList();
    }

    public List<VerifiedUrgentDonorDTO> getVerifiedUrgentDonors() {
        return urgentDonorRegistryRepository.findByIsVerifiedTrueAndIsAvailableTrue()
                .stream()
                .map(urgentDonorMapper::toVerifiedDTO)
                .toList();
    }

    public void verifyDonor(Long id) {
        UrgentDonorRegistry donor = urgentDonorRegistryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người hiến máu"));

        donor.setIsVerified(true);
        donor.setIsAvailable(true);
        urgentDonorRegistryRepository.save(donor);

        // Gửi email xác minh thành công
        UserProfile profile = donor.getDonor().getUserProfile();
        String fullName = profile != null ? profile.getFullName() : "bạn";
        String email = donor.getDonor().getEmail();
        String subject = "Xác minh hiến máu khẩn cấp thành công";
        String body = """
                Xin chào %s,

                Hồ sơ đăng ký hiến máu khẩn cấp của bạn đã được xác minh thành công.
                Cảm ơn bạn vì tinh thần sẵn sàng hỗ trợ cộng đồng.

                Trân trọng,
                Hệ thống hỗ trợ hiến máu.
                """.formatted(fullName);
        emailService.sendSimpleMessage(email, subject, body);
    }

    public void rejectDonor(Long id) {
        UrgentDonorRegistry donor = urgentDonorRegistryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người hiến máu"));

        if (Boolean.TRUE.equals(donor.getIsVerified())) {
            throw new IllegalStateException("Người hiến máu đã được xác minh, không thể từ chối");
        }

        urgentDonorRegistryRepository.deleteById(id);
    }

    // Bước 6: Nếu người dùng muốn rời khỏi nhóm hiến máu khẩn cấp:
    public void leaveUrgentDonorGroup(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        UrgentDonorRegistry registry = urgentDonorRegistryRepo.findByDonor(user)
                .orElseThrow(() -> new ResourceNotFoundException("❗Bạn không nằm trong danh sách hiến máu khẩn cấp."));

        if (!Boolean.TRUE.equals(registry.getIsAvailable())) {
            throw new IllegalStateException("❗Bạn đã rút khỏi danh sách trước đó.");
        }

        registry.setIsAvailable(false);
        registry.setReadinessLevel(DonorReadinessLevel.UNDECIDED); // 👈 Quan trọng: chuyển về mức sẵn sàng thường
        registry.setLeftGroupAt(LocalDateTime.now());  // 👈 Có thể thêm cột này trong entity
        urgentDonorRegistryRepo.save(registry);
    }




    public String getDonorStatus(Long userId) {
        return urgentDonorRegistryRepository.findByDonor_UserId(userId)
                .map(donor -> {
                    if (Boolean.TRUE.equals(donor.getIsVerified())) {
                        return "VERIFIED";
                    } else {
                        return "PENDING";
                    }
                })
                .orElse("NOT_REGISTERED");
    }

    public ReadinessConflictCheckDTO checkConflict(Long userId, DonorReadinessLevel newMode) {
        Optional<UrgentDonorRegistry> optional = urgentDonorRegistryRepo.findByDonor_UserId(userId);

        if (optional.isEmpty()) {
            return new ReadinessConflictCheckDTO(false, null); // chưa từng đăng ký → không xung đột
        }

        UrgentDonorRegistry registry = optional.get();

        boolean conflict = Boolean.TRUE.equals(registry.getIsAvailable())
                && registry.getReadinessLevel() != null
                && !registry.getReadinessLevel().equals(newMode);

        String current = registry.getReadinessLevel() != null ? registry.getReadinessLevel().name() : null;

        return new ReadinessConflictCheckDTO(conflict, current);
    }


    public String forceChangeReadinessLevel(Long userId, DonorReadinessLevel newMode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        UrgentDonorRegistry registry = urgentDonorRegistryRepo.findByDonor(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không có đơn đăng ký hiến máu khẩn cấp"));

        DonorReadinessLevel oldMode = registry.getReadinessLevel();

        // ✅ Nếu giống nhau thì không cần update, trả về thông báo
        if (oldMode == newMode) {
            return "⚠️ Bạn đang ở cùng chế độ: " + newMode.name() + " (không có thay đổi)";
        }

        // ✅ Nếu khác nhau thì update
        registry.setReadinessLevel(newMode);
        registry.setIsAvailable(true);
        registry.setIsVerified(false);
        registry.setRegisteredAt(LocalDateTime.now());
        registry.setLeftGroupAt(null);
        urgentDonorRegistryRepo.save(registry);

        // ✅ Ghi log thay đổi
        ReadinessChangeLog log = new ReadinessChangeLog();
        log.setDonor(user);
        log.setFromLevel(oldMode);
        log.setToLevel(newMode);
        log.setChangedAt(LocalDateTime.now());
        readinessChangeLogRepository.save(log);

        return "✅ Đã cập nhật trạng thái readiness thành công";
    }

    public List<ReadinessChangeLogDTO> getReadinessChangeLogsByUserIdAndDate(
            Long userId, LocalDate from, LocalDate to) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        LocalDateTime fromDate = from != null ? from.atStartOfDay() : null;
        LocalDateTime toDate = to != null ? to.atTime(23, 59, 59) : null;

        return readinessChangeLogRepository.findByUserAndDateRange(user, fromDate, toDate).stream()
                .map(log -> new ReadinessChangeLogDTO(
                        log.getFromLevel(),        // ✅ field đúng
                        log.getToLevel(),          // ✅ field đúng
                        log.getChangedAt()
                ))
                .toList();

    }
    public CurrentUrgentDonorStatusDTO getCurrentUrgentStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        UrgentDonorRegistry registry = urgentDonorRegistryRepo.findByDonor(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không có đơn đăng ký hiến máu khẩn cấp"));

        return new CurrentUrgentDonorStatusDTO(
                registry.getReadinessLevel(),
                registry.getRegisteredAt(),
                registry.getIsVerified(),
                registry.getLeftGroupAt()
        );
    }

}
