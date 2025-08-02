package com.quyet.superapp.config;

import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.enums.RoleEnum;
import com.quyet.superapp.repository.*;
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


    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initRoles();
        initBloodTypes();
        initBloodComponents();
        initBloodPrices();
        initAdminUser();
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
