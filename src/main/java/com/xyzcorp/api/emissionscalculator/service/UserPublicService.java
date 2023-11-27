package com.xyzcorp.api.emissionscalculator.service;

import com.xyzcorp.api.emissionscalculator.dto.UserAuthRequestDto;
import com.xyzcorp.api.emissionscalculator.dto.UserAuthResponseDto;
import com.xyzcorp.api.emissionscalculator.dto.UserDto;
import com.xyzcorp.api.emissionscalculator.entity.User;
import com.xyzcorp.api.emissionscalculator.exception.AuthenticationFailedException;
import com.xyzcorp.api.emissionscalculator.exception.UserExistsException;
import com.xyzcorp.api.emissionscalculator.repository.UserRepository;
import com.xyzcorp.api.emissionscalculator.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
@Slf4j
public class UserPublicService {

    private UserRepository userRepository;
    private UserRoleService userRoleService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;

    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseEntity register(UserDto dto) {
        if(isNull(userRepository.findByEmailAddress(dto.getEmailAddress()))) {
            User user = userRepository.save(User.builder()
                    .name(dto.getName())
                    .emailAddress(dto.getEmailAddress())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .role(userRoleService.get(dto.getRole()))
                    .createdOn(LocalDateTime.now())
                    .build());
            return ResponseEntity.ok(Map.of("id", user.getId()));
        }
        throw new UserExistsException("User email address already exists", "E108", HttpStatus.BAD_REQUEST);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserAuthResponseDto authenticate(UserAuthRequestDto requestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmailAddress().trim(),
                            requestDto.getPassword().trim()));

            return generateAuthResponse(userRepository.findByEmailAddress(requestDto.getEmailAddress().trim()));

        } catch (Exception e) {
            log.error(format("User %s has been ***failed*** to authenticate. Error [%s]", requestDto.getEmailAddress(), e.getMessage()));
        }

        throw new AuthenticationFailedException("Authentication failed", "E109", HttpStatus.BAD_REQUEST);
    }

    private UserAuthResponseDto generateAuthResponse(User user) {
        return UserAuthResponseDto.builder()
                .emailAddress(user.getEmailAddress())
                .name(user.getName())
                .token(jwtTokenProvider.createToken(user.getName(), user.getEmailAddress()))
                .build();
    }
}
