package com.xyzcorp.api.emissionscalculator.controller;

import com.xyzcorp.api.emissionscalculator.dto.GenericResponseDto;
import com.xyzcorp.api.emissionscalculator.service.CompanyFleetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/private/v1/company/{companyId}/fleet")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CompanyFleetController {

    @Autowired
    private CompanyFleetService companyFleetService;

    @PostMapping()
    @Operation(summary = "Upload company's fleet data. " +
            "File format should be an EXCEL sheet, with column of order Employee ID, Vehicle type and Average monthly mileage." +
            " First row of the sheet will be ignored always.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When the file has been processed successfully.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "When an invalid file is uploaded.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "When the company not found.",
                    content = { @Content(schema = @Schema(implementation = GenericResponseDto.class)) }) })
    public ResponseEntity upload(@PathVariable String companyId,
                                 @RequestParam("file") MultipartFile multipartFile) {
        return companyFleetService.persist(Long.valueOf(companyId), multipartFile);
    }

}
