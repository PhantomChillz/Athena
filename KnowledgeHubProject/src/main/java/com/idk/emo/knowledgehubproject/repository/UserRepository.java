package com.idk.emo.knowledgehubproject.repository;

import com.idk.emo.knowledgehubproject.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM users u JOIN users_roles ur ON u.user_id = ur.user_id JOIN roles r ON ur.role_id = r.role_id WHERE u.username = :username LIMIT 1", nativeQuery = true)
    Optional<User> findByUsernameWithRoles(@Param("username") String username);



}
