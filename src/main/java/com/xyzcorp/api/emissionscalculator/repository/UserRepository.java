package com.xyzcorp.api.emissionscalculator.repository;

import com.xyzcorp.api.emissionscalculator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmailAddress(String emailAddress);
}
