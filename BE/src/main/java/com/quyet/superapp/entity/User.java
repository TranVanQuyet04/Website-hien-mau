package com.quyet.superapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_Id")
    private Long userId;


    @Column(name = "UserName", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    private String username;

    @Column(name = "Password", columnDefinition = "VARCHAR(255)", nullable = false)
    private String password;

    @Column(name = "IsEnable")
    private boolean enable;

    @Column(name = "Email", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String email;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Role_Id")
    private Role role;


    // ✅ Giữ lại duy nhất profile
    @OneToOne(mappedBy = "user", cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH
    }, fetch = FetchType.LAZY, orphanRemoval = true)

    @JsonIgnore // ✅ TRÁNH vòng lặp khi trả về user → profile → user...
    private UserProfile userProfile;

    public UserProfile getProfile() {
        return userProfile;
    }

    public User(Long userId) {
        this.userId = userId;
    }

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
