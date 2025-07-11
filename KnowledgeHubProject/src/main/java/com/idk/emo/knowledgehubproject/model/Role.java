package com.idk.emo.knowledgehubproject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Long roleId;

    @Column(name="role_name")
    @Enumerated(EnumType.STRING)
    private AppRole role;

    public Role(AppRole appRole) {
        this.role = appRole;
    }
}
