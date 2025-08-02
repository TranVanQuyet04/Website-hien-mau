package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Role;
import com.quyet.superapp.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // 🔍 Tìm role theo tên (ADMIN, STAFF, MEMBER...)
    Optional<Role> findByName(String name);
}
