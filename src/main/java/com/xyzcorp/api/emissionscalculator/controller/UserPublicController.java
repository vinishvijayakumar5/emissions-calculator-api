package com.xyzcorp.api.emissionscalculator.controller;

import com.xyzcorp.api.emissionscalculator.dto.GenericResponseDto;
import com.xyzcorp.api.emissionscalculator.dto.UserAuthRequestDto;
import com.xyzcorp.api.emissionscalculator.dto.UserAuthResponseDto;
import com.xyzcorp.api.emissionscalculator.dto.UserDto;
import com.xyzcorp.api.emissionscalculator.service.UserPublicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/public/v1/user")
public class UserPublicController {

    @Autowired
    private UserPublicService userPublicService;

    @PostMapping("/register")
    @Operation(summary = "Register user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When user has been registered successfully.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "When user email address already exists.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }) })
    public ResponseEntity register(@RequestBody UserDto dto) {
        return userPublicService.register(dto);
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When authentication is success.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "When authentication failed.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }) })
    public UserAuthResponseDto authenticate(@RequestBody UserAuthRequestDto dto) {
        return userPublicService.authenticate(dto);
    }

}
