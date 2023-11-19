package com.xyzcorp.api.emissionscalculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.dto.CompanyDto;
import com.xyzcorp.api.emissionscalculator.entity.Company;
import com.xyzcorp.api.emissionscalculator.exception.CompanyExistsException;
import com.xyzcorp.api.emissionscalculator.exception.CompanyNotFoundException;
import com.xyzcorp.api.emissionscalculator.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyRepository companyRepository;
    private ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseEntity persist(CompanyDto dto) {
        if(!isNameExists(dto)) {
            Company company = companyRepository.save(map(dto));
            return ResponseEntity.ok(Map.of("id", company.getId()));
        }
        throw new CompanyExistsException("Company name exists", "E100", HttpStatus.BAD_REQUEST);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CompanyDto get(long id) {
       return map(companyRepository.findById(id));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Company getCompany(long id) {
        Optional<Company> company = companyRepository.findById(id);
        if(company.isPresent()) {
            return company.get();
        }
        throw new CompanyNotFoundException("Company not found", "E101", HttpStatus.NOT_FOUND);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean isExists(long id) {
        return nonNull(get(id));
    }

    public boolean isNameExists(CompanyDto dto) {
        return nonNull(companyRepository.findByName(dto.getName()));
    }

    private Company map(CompanyDto dto) {
        Company company = objectMapper.convertValue(dto, Company.class);
        company.setCreatedOn(LocalDateTime.now());
        return company;
    }

    private CompanyDto map(Optional<Company> company) {
        if(company.isPresent()) {
            return objectMapper.convertValue(company.get(), CompanyDto.class);
        }
        throw new CompanyNotFoundException("Company not found", "E101", HttpStatus.NOT_FOUND);
    }
}
