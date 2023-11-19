package com.xyzcorp.api.emissionscalculator.repository;

import com.xyzcorp.api.emissionscalculator.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByName(String name);
}
