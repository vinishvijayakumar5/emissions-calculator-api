package com.xyzcorp.api.emissionscalculator.service;

import com.xyzcorp.api.emissionscalculator.dto.UserAuthResponseDto;
import com.xyzcorp.api.emissionscalculator.exception.AuthenticationFailedException;
import com.xyzcorp.api.emissionscalculator.exception.UserExistsException;
import com.xyzcorp.api.emissionscalculator.repository.UserRepository;
import com.xyzcorp.api.emissionscalculator.security.JwtTokenProvider;
import com.xyzcorp.api.emissionscalculator.testdata.TestUserDataManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPublicServiceTest implements TestUserDataManager {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserPublicService userPublicService;

    @Test
    void test_register_whenGivenValidData_thenReturnUserId() {
        when(userRepository.findByEmailAddress(anyString())).thenReturn(null);
        when(userRepository.save(any())).thenReturn(user("test@test.com"));

        ResponseEntity responseEntity = userPublicService.register(userDto("test@test.com"));

        assertNotNull(responseEntity);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(responseEntity.hasBody());

        var body = (Map) responseEntity.getBody();

        assertEquals(1L, body.get("id"));
    }

    @Test
    void test_register_whenGivenDuplicateData_thenThrowUserExistsException() {
        when(userRepository.findByEmailAddress(anyString())).thenReturn(user("test@test.com"));

        UserExistsException userExistsException = assertThrows(UserExistsException.class,
                () -> userPublicService.register(userDto("test@test.com")));

        assertEquals("User email address already exists", userExistsException.getMessage());
        assertEquals("E108", userExistsException.getContract());
        assertEquals(HttpStatus.BAD_REQUEST, userExistsException.getStatus());
    }

    @Test
    void test_authenticate_whenGivenValidData_thenReturnAuthResponseWithToken() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmailAddress(anyString())).thenReturn(user("test@test.eu"));
        when(jwtTokenProvider.createToken(anyString(), anyString())).thenReturn("token");

        UserAuthResponseDto response = userPublicService.authenticate(userAuthRequestDto("test@test.eu"));

        assertNotNull(response);
        assertEquals("token", response.getToken());
        assertEquals("test@test.eu", response.getEmailAddress());
    }

    @Test
    void test_authenticate_whenGivenInvalidData_thenThrowAuthenticationFailedException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new AuthenticationFailedException("Authentication failed", "E109",
                        HttpStatus.BAD_REQUEST));

        AuthenticationFailedException authenticationFailedException = assertThrows(AuthenticationFailedException.class,
                () -> userPublicService.authenticate(userAuthRequestDto("test@test.eu")));

        assertEquals("Authentication failed", authenticationFailedException.getMessage());
        assertEquals("E109", authenticationFailedException.getContract());
        assertEquals(HttpStatus.BAD_REQUEST, authenticationFailedException.getStatus());
    }

    @Test
    void authenticate() {
    }
}