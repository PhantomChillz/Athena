package com.idk.emo.knowledgehubproject.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(unique = true)
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"
            )
    )
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<Note> notes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<FileEntity> files = new ArrayList<>();

}
