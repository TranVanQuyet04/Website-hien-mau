package com.quyet.superapp.config;

import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.City;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.enums.RoleEnum;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.repository.address.CityRepository;
import com.quyet.superapp.repository.address.DistrictRepository;
import com.quyet.superapp.repository.address.WardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final BloodTypeRepository bloodTypeRepo;
    private final BloodComponentRepository componentRepo;
    private final BloodPriceRepository priceRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final OccupationRepository occupationRepo;
    private final CityRepository cityRepo;
    private final DistrictRepository districtRepo;
    private final WardRepository wardRepo;



    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initRoles();
        initBloodTypes();
        initBloodComponents();
        initBloodPrices();
        initOccupations();
        initAddresses();
        initAdminUser();
    }

    private void initAddresses() {
        // Seed tối thiểu để UI có dữ liệu chọn quận/phường ngay trên môi trường production.
        // Idempotent: chạy lại không tạo bản ghi trùng.
        City hcm = cityRepo.findByNameCity("TP Hồ Chí Minh")
                .orElseGet(() -> cityRepo.save(new City(null, "TP Hồ Chí Minh")));

        record DistrictSeed(String districtName, int wardCount) {}
        List<DistrictSeed> districts = List.of(
                new DistrictSeed("Quận 1", 10),
                new DistrictSeed("Quận 3", 10),
                new DistrictSeed("Quận 4", 10),
                new DistrictSeed("Quận 5", 10),
                new DistrictSeed("Quận 6", 10),
                new DistrictSeed("Quận 7", 10),
                new DistrictSeed("Quận 8", 10),
                new DistrictSeed("Quận 10", 10),
                new DistrictSeed("Quận 11", 10),
                new DistrictSeed("Quận 12", 10),
                new DistrictSeed("Quận Bình Thạnh", 15),
                new DistrictSeed("Quận Gò Vấp", 15),
                new DistrictSeed("Quận Phú Nhuận", 10),
                new DistrictSeed("Quận Tân Bình", 15),
                new DistrictSeed("Quận Tân Phú", 15),
                new DistrictSeed("TP Thủ Đức", 20),
                new DistrictSeed("Quận Bình Tân", 15),
                new DistrictSeed("Huyện Bình Chánh", 10),
                new DistrictSeed("Huyện Củ Chi", 10),
                new DistrictSeed("Huyện Hóc Môn", 10),
                new DistrictSeed("Huyện Nhà Bè", 10),
                new DistrictSeed("Huyện Cần Giờ", 7)
        );

        int insertedDistricts = 0;
        int insertedWards = 0;

        for (DistrictSeed seed : districts) {
            if (!districtRepo.existsByDistrictNameIgnoreCaseAndCity_CityId(seed.districtName(), hcm.getCityId())) {
                District d = new District();
                d.setDistrictName(seed.districtName());
                d.setCity(hcm);
                districtRepo.save(d);
                insertedDistricts++;
            }
        }

        // Load lại districts để có districtId
        List<District> persistedDistricts = districtRepo.findByCity_CityId(hcm.getCityId());
        Map<String, District> districtByName = new HashMap<>();
        for (District d : persistedDistricts) {
            districtByName.put(d.getDistrictName().toLowerCase(Locale.ROOT), d);
        }

        for (DistrictSeed seed : districts) {
            District d = districtByName.get(seed.districtName().toLowerCase(Locale.ROOT));
            if (d == null) continue;

            for (int i = 1; i <= seed.wardCount(); i++) {
                String wardName = (seed.districtName().startsWith("Huyện") ? "Xã " : "Phường ") + i;
                if (wardRepo.existsByWardNameIgnoreCaseAndDistrict_DistrictId(wardName, d.getDistrictId())) {
                    continue;
                }
                Ward w = new Ward();
                w.setWardName(wardName);
                w.setDistrict(d);
                wardRepo.save(w);
                insertedWards++;
            }
        }

        log.info("📍 Địa chỉ: TP.HCM (cityId={}) - thêm {} quận/huyện, {} phường/xã.",
                hcm.getCityId(), insertedDistricts, insertedWards);
    }

    private void initOccupations() {
        List<String> occupations = List.of(
                "Bác sĩ", "Bảo vệ", "Bộ đội", "Buôn bán", "Ca sỹ", "Cán bộ", "Cán bộ nhân viên",
                "Cảnh sát", "Chiến sĩ", "Chuyên viên", "Công an", "Công nhân", "Công nhân viên chức",
                "Cử nhân xét nghiệm", "Diễn viên", "Điều dưỡng", "Đoàn viên", "Dược sỹ", "Giám đốc",
                "Giáo viên", "Hộ lý", "Học sinh", "Học viên", "Hướng dẫn viên du lịch", "Hưu trí",
                "Kế toán", "Khác", "Kiến trúc sư", "Kinh doanh", "Kỹ sư", "Kỹ thuật viên", "Lái xe",
                "Làm biển", "Làm nông", "Làm thuê", "Luật sư", "Nhân viên", "Nhân viên kinh doanh",
                "Nhân viên văn phòng", "Nội trợ", "Nữ hộ sinh", "Nv mạng máy vi tính", "Phiên dịch",
                "Phó giám đốc", "Phóng viên", "Quản lý", "Sinh viên", "Sửa xe", "Thiết kế", "Thợ cắt tóc",
                "Thợ điện", "Thợ hàn", "Thợ hồ", "Thợ may", "Thợ máy", "Thợ sơn", "Thư ký", "Tiếp viên",
                "Tự do", "Tu sĩ", "Uốn tóc", "Văn thư", "Viên chức", "Xây dựng", "Y công", "Y sĩ đa khoa",
                "Y tá"
        );

        int inserted = 0;
        int skipped = 0;

        for (String name : occupations) {
            if (occupationRepo.existsByName(name)) {
                skipped++;
                continue;
            }
            occupationRepo.save(new Occupation(null, name));
            inserted++;
        }

        log.info("📄 Nghề nghiệp: thêm {}, bỏ qua {} (đã tồn tại).", inserted, skipped);
    }



    private void initRoles() {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            roleRepo.findByName(String.valueOf(roleEnum)).orElseGet(() -> {
                Role role = new Role();
                role.setName(String.valueOf(roleEnum));
                return roleRepo.save(role);
            });
        }
        log.info("🔐 Đã khởi tạo vai trò: {}", Arrays.toString(RoleEnum.values()));
    }



    private void initAdminUser() {
        String username = "admin123";
        String email = "tranquyetcoder@gmail.com";
        if (userRepo.existsByUsername(username)) {
            log.info("👤 User admin '{}' đã tồn tại, bỏ qua.", username);
            return;
        }

        Role adminRole = roleRepo.findByName(String.valueOf(RoleEnum.ADMIN))
                .orElseGet(() -> roleRepo.save(new Role(null, RoleEnum.ADMIN)));

        User admin = User.builder()
                .username(username)
                .password(passwordEncoder.encode("admin123"))
                .email(email)
                .role(adminRole)
                .enable(true)
                .build();

        userRepo.save(admin);
        log.info("✅ Đã tạo user admin mặc định: {} / {}", username, "admin123");
    }




    private void initBloodTypes() {
        record BloodGroup(String desc, String rh, String note) {}
        List<BloodGroup> groups = List.of(
                new BloodGroup("A+", "+", "Phổ biến"),
                new BloodGroup("A-", "-", null),
                new BloodGroup("B+", "+", "Phổ biến"),
                new BloodGroup("B-", "-", null),
                new BloodGroup("AB+", "+", "Hiếm"),
                new BloodGroup("AB-", "-", "Rất hiếm"),
                new BloodGroup("O+", "+", "Phổ biến"),
                new BloodGroup("O-", "-", "Toàn năng")
        );

        for (BloodGroup g : groups) {
            if (!bloodTypeRepo.existsByDescriptionAndRh(g.desc(), g.rh())) {
                bloodTypeRepo.save(BloodType.builder()
                        .description(g.desc())
                        .rh(g.rh())
                        .note(g.note())
                        .isActive(true)
                        .build());
                log.info("🩸 Thêm nhóm máu mới: {}{}", g.desc(), g.rh());
            }
        }
    }

    private void initBloodComponents() {
        Map<String, BloodComponent> defaultComponents = Map.of(
                "PRC", BloodComponent.builder()
                        .code("PRC")
                        .name("Hồng cầu")
                        .storageTemp("2-6°C")
                        .storageDays(42)
                        .usage("Thiếu máu, mất máu nặng")
                        .isApheresisCompatible(true)
                        .type(BloodComponentType.RED_BLOOD_CELLS)
                        .isActive(true)
                        .build(),

                "PLT", BloodComponent.builder()
                        .code("PLT")
                        .name("Tiểu cầu")
                        .storageTemp("20-24°C")
                        .storageDays(5)
                        .usage("Giảm tiểu cầu, xuất huyết")
                        .isApheresisCompatible(true)
                        .type(BloodComponentType.PLATELETS)
                        .isActive(true)
                        .build(),

                "FFP", BloodComponent.builder()
                        .code("FFP")
                        .name("Huyết tương")
                        .storageTemp("-18°C")
                        .storageDays(365)
                        .usage("Đông máu, sốc mất máu")
                        .isApheresisCompatible(false)
                        .type(BloodComponentType.PLASMA)
                        .isActive(true)
                        .build()
        );

        for (String code : defaultComponents.keySet()) {
            if (!componentRepo.existsByCode(code)) {
                componentRepo.save(defaultComponents.get(code));
                log.info("🧬 Thêm thành phần máu: {}", code);
            }
        }
    }

    private void initBloodPrices() {
        List<BloodType> types = bloodTypeRepo.findAll();
        List<BloodComponent> components = componentRepo.findAll();

        if (types.isEmpty() || components.isEmpty()) {
            log.warn("❌ Không thể khởi tạo bảng giá: thiếu nhóm máu hoặc thành phần máu.");
            return;
        }

        int inserted = 0;
        int skipped = 0;

        for (BloodType type : types) {
            for (BloodComponent component : components) {
                boolean exists = priceRepo.findByBloodTypeAndBloodComponent(type, component).isPresent();
                if (exists) {
                    skipped++;
                    continue;
                }

                double price = switch (component.getCode()) {
                    case "PRC" -> 300_000;
                    case "PLT" -> 450_000;
                    case "FFP" -> 200_000;
                    default -> 0;
                };

                if (price > 0) {
                    BloodPrice bp = BloodPrice.builder()
                            .bloodType(type)
                            .bloodComponent(component)
                            .pricePerBag(price)
                            .build();
                    priceRepo.save(bp);
                    inserted++;
                }
            }
        }

        log.info("💰 Đã thêm {} dòng giá máu. Bỏ qua {} dòng đã có.", inserted, skipped);
    }
}
