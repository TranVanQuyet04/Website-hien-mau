package com.quyet.superapp.entity;

import com.quyet.superapp.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Roles")
@Data
@ToString(exclude = "users")
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Role_Id")
    private Long roleId;

    @Column(name = "Name", nullable = false, unique = true, length = 50)
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<User> users;

    // ✅ Constructor thêm để tạo Role mới bằng tên (ví dụ: "ADMIN")
    public Role(String name, RoleEnum admin) {
        this.name = name;
    }
}
