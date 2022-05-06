package com.example.dailyplanner.repository;

import com.example.dailyplanner.model.Role;
import com.example.dailyplanner.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);

    List<Role> findAll();
}
