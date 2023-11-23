package com.xyzcorp.api.emissionscalculator.testdata;

import com.xyzcorp.api.emissionscalculator.dto.UserAuthRequestDto;
import com.xyzcorp.api.emissionscalculator.dto.UserDto;
import com.xyzcorp.api.emissionscalculator.entity.User;
import com.xyzcorp.api.emissionscalculator.entity.UserRole;
import com.xyzcorp.api.emissionscalculator.utils.Role;

import java.time.LocalDateTime;

public interface TestUserDataManager {

    String NAME = "Admin";
    String PASSWORD = "Welcome@123";
    String BEARER = "Bearer ";
    String EXISTING_USER_EMAIL_ADDRESS = "admin_test@xyz.corp";

    default User user(String emailAddress) {
        return User.builder()
                .name("Addeee")
                .emailAddress(emailAddress)
                .createdOn(LocalDateTime.now())
                .password("Welcome@q23")
                .id(1)
                .role(UserRole.builder()
                        .id(1)
                        .name("ROLE_ADMIN")
                        .build())
                .build();
    }
     default UserDto userDto(String emailAddress) {
        return UserDto.builder()
                .name(NAME)
                .emailAddress(emailAddress)
                .password(PASSWORD)
                .role(Role.ROLE_ADMIN.name())
                .build();
    }

    default UserAuthRequestDto userAuthRequestDto(String emailAddress) {
        return UserAuthRequestDto.builder()
                .emailAddress(emailAddress)
                .password(PASSWORD)
                .build();
    }

    default String bearerToken(String token) {
         return BEARER.concat(token);
    }

}
