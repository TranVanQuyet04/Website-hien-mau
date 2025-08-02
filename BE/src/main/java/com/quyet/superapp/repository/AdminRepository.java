package com.quyet.superapp.repository;

import com.quyet.superapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.email FROM User u WHERE u.role.name = 'ADMIN' AND u.email IS NOT NULL")
    List<String> findAllAdminEmails();
}
