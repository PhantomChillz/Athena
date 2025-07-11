package com.idk.emo.knowledgehubproject.repository;

import com.idk.emo.knowledgehubproject.model.AppRole;
import com.idk.emo.knowledgehubproject.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRole(AppRole appRole);
}
