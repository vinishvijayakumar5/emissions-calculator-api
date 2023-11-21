package com.xyzcorp.api.emissionscalculator.controller;

import com.xyzcorp.api.emissionscalculator.dto.CompanyDto;
import com.xyzcorp.api.emissionscalculator.dto.GenericResponseDto;
import com.xyzcorp.api.emissionscalculator.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/private/v1/company")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    @Operation(summary = "Create a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When the company has been created successfully.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "When the company name already exists.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }) })
    public ResponseEntity create(@RequestBody CompanyDto dto) {
        return companyService.persist(dto);
    }

}
