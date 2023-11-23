package com.xyzcorp.api.emissionscalculator.controller;

import com.xyzcorp.api.emissionscalculator.dto.EmployeeEmissionDto;
import com.xyzcorp.api.emissionscalculator.dto.GenericResponseDto;
import com.xyzcorp.api.emissionscalculator.dto.VehicleAlternative;
import com.xyzcorp.api.emissionscalculator.service.EmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/private/v1/company/{companyId}/emission")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class EmissionController {

    @Autowired
    private EmissionService emissionService;

    @GetMapping()
    @Operation(summary = "Get emissions by company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When emissions data found."),
            @ApiResponse(responseCode = "404", description = "When no emissions data found.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }) })
    public List<EmployeeEmissionDto> get(@PathVariable String companyId) {
        return emissionService.get(companyId);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get emissions by employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When emissions data found."),
            @ApiResponse(responseCode = "404", description = "When no emissions data found.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }) })
    public List<EmployeeEmissionDto> get(@PathVariable String companyId, @PathVariable String employeeId) {
        return emissionService.get(companyId, employeeId);
    }

    @GetMapping("/employee/{employeeId}/alternatives")
    @Operation(summary = "Get vehicle alternatives by employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When emissions data found."),
            @ApiResponse(responseCode = "404", description = "When no emissions data found.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }) })
    public List<VehicleAlternative> getAlternatives(@PathVariable String companyId, @PathVariable String employeeId) {
        return emissionService.getAlternatives(companyId, employeeId);
    }

}
