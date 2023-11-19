package com.xyzcorp.api.emissionscalculator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.dto.CompanyFleetDto;
import com.xyzcorp.api.emissionscalculator.entity.Company;
import com.xyzcorp.api.emissionscalculator.entity.CompanyFleet;
import com.xyzcorp.api.emissionscalculator.exception.DataNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Component
@AllArgsConstructor
public class CompanyFleetMapper {

    private ObjectMapper objectMapper;

    public CompanyFleet map(CompanyFleetDto dto, Company company) {
        CompanyFleet companyFleet = objectMapper.convertValue(dto, CompanyFleet.class);
        companyFleet.setCreatedOn(LocalDateTime.now());
        companyFleet.setCompany(company);
        return companyFleet;
    }

    public List<CompanyFleet> map(List<CompanyFleetDto> fleets, Company company) {
        return emptyIfNull(fleets).stream()
                .map(f -> map(f, company))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<CompanyFleetDto> map(List<CompanyFleet> fleets) {
        return emptyIfNull(fleets).stream()
                .map(f -> map(f))
                .collect(Collectors.toUnmodifiableList());
    }

    public CompanyFleetDto map(CompanyFleet companyFleet) {
        return objectMapper.convertValue(companyFleet, CompanyFleetDto.class);
    }

    public CompanyFleetDto map(Optional<CompanyFleet> companyFleet) {
        if(companyFleet.isPresent()) {
            return objectMapper.convertValue(companyFleet.get(), CompanyFleetDto.class);
        }
        throw new DataNotFoundException("Company's fleet not found", "E103", HttpStatus.NOT_FOUND);
    }
}
