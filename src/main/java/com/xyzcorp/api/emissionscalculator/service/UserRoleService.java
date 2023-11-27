package com.xyzcorp.api.emissionscalculator.service;

import com.xyzcorp.api.emissionscalculator.entity.UserRole;
import com.xyzcorp.api.emissionscalculator.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserRole get(String name) {
        return userRoleRepository.findByName(name);
    }
}
