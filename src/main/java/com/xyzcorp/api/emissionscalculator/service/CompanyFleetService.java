package com.xyzcorp.api.emissionscalculator.service;

import com.xyzcorp.api.emissionscalculator.dto.CompanyFleetDto;
import com.xyzcorp.api.emissionscalculator.dto.GenericResponseDto;
import com.xyzcorp.api.emissionscalculator.entity.Company;
import com.xyzcorp.api.emissionscalculator.exception.InvalidRequestException;
import com.xyzcorp.api.emissionscalculator.mapper.CompanyFleetMapper;
import com.xyzcorp.api.emissionscalculator.repository.CompanyFleetRepository;
import com.xyzcorp.api.emissionscalculator.utils.FleetFileOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.BiFunction;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class CompanyFleetService {

    private CompanyService companyService;
    private CompanyFleetRepository companyFleetRepository;
    private CompanyFleetMapper companyFleetMapper;
    private FleetFileOperations fleetFileOperations;

    @Autowired
    public CompanyFleetService(
            CompanyService companyService,
            CompanyFleetRepository companyFleetRepository,
            CompanyFleetMapper companyFleetMapper,
            FleetFileOperations fleetFileOperations) {
        this.companyService = companyService;
        this.companyFleetRepository = companyFleetRepository;
        this.companyFleetMapper = companyFleetMapper;
        this.fleetFileOperations = fleetFileOperations;
    }

    private final BiFunction<Long, MultipartFile, Company> ACCEPTANCE = ((companyId, file) -> {
        Company company = companyService.getCompany(companyId);
        if(isNull(company) || isNull(file)) {
            throw new InvalidRequestException("Invalid file.", "E102", HttpStatus.BAD_REQUEST);
        }
        return company;
    });

    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseEntity persist(long companyId, MultipartFile multipartFile) {
        // verify company id and file
        Company company = ACCEPTANCE.apply(companyId, multipartFile);
        // parse file
        List<CompanyFleetDto> fleets = fleetFileOperations.parse(multipartFile);
        // persist
        companyFleetRepository.saveAll(companyFleetMapper.map(fleets, company));

        return ResponseEntity.ok(getSuccessResponse());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CompanyFleetDto> get(long companyId, String employeeId) {
       return companyFleetMapper.map(companyFleetRepository.findByCompanyIdAndEmployeeId(companyId, employeeId));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<CompanyFleetDto> get(long companyId) {
        return companyFleetMapper.map(companyFleetRepository.findByCompanyId(companyId));
    }

    private GenericResponseDto getSuccessResponse() {
        return new GenericResponseDto(true,
                "Company fleet has been processed successfully.",
                "S101");
    }

}
