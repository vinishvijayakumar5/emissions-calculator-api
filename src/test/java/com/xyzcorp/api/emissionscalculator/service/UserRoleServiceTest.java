package com.xyzcorp.api.emissionscalculator.service;

import com.xyzcorp.api.emissionscalculator.entity.UserRole;
import com.xyzcorp.api.emissionscalculator.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {

    @Mock
    private UserRoleRepository userRoleRepository;
    @InjectMocks
    private UserRoleService userRoleService;

    @Test
    void test_getRoles() {
        when(userRoleRepository.findByName(anyString()))
                .thenReturn(UserRole.builder().name("ROLE_ADMIN").build());

        UserRole userRole = userRoleService.get("ROLE_ADMIN");

        assertNotNull(userRole);
        assertEquals("ROLE_ADMIN", userRole.getName());
    }
}