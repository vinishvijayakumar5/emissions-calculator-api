package com.xyzcorp.api.emissionscalculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.dto.CompanyDto;
import com.xyzcorp.api.emissionscalculator.entity.Company;
import com.xyzcorp.api.emissionscalculator.exception.CompanyExistsException;
import com.xyzcorp.api.emissionscalculator.exception.CompanyNotFoundException;
import com.xyzcorp.api.emissionscalculator.repository.CompanyRepository;
import com.xyzcorp.api.emissionscalculator.testdata.TestCompanyDataManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest implements TestCompanyDataManager {

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private CompanyService companyService;

    @Test
    void test_persist_whenGivenValidCompany_thenReturnSuccess() {
        when(companyRepository.findByName(anyString())).thenReturn(null);
        when(companyRepository.save(any())).thenReturn(companyStore());
        when(objectMapper.convertValue(any(), eq(Company.class)))
                .thenReturn(companyStore());

        ResponseEntity responseEntity = companyService.persist(companyDto());

        assertNotNull(responseEntity);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(responseEntity.hasBody());

        var body = (Map) responseEntity.getBody();

        assertEquals(1L, body.get("id"));

    }

    @Test
    void test_persist_whenGivenDuplicateCompany_thenThrowCompanyExistsException() {
        when(companyRepository.findByName(anyString())).thenReturn(companyStore());

        CompanyExistsException companyExistsException = assertThrows(CompanyExistsException.class,
                () -> companyService.persist(companyDto()));

        assertEquals("Company name exists", companyExistsException.getMessage());
        assertEquals("E100", companyExistsException.getContract());
        assertEquals(HttpStatus.BAD_REQUEST, companyExistsException.getStatus());;
    }

    @Test
    void test_getCompany_whenGivenValidCompanyId_thenReturnCompany() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyStore()));
        when(objectMapper.convertValue(any(), eq(CompanyDto.class)))
                .thenReturn(companyDto());

        CompanyDto dto = companyService.get(1);

        assertNotNull(dto);
        assertEquals(companyDto().getName(), dto.getName());
        assertEquals(companyDto().getType(), dto.getType());
        assertEquals(companyDto().getCity(), dto.getCity());
        assertEquals(companyDto().getCountry(), dto.getCountry());
    }

    @Test
    void test_getCompany_whenGivenInvalidCompanyId_thenThrowCompanyNotFoundException() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        CompanyNotFoundException companyNotFoundException = assertThrows(CompanyNotFoundException.class,
                () -> companyService.get(1));

        assertEquals("Company not found", companyNotFoundException.getMessage());
        assertEquals("E101", companyNotFoundException.getContract());
        assertEquals(HttpStatus.NOT_FOUND, companyNotFoundException.getStatus());
    }

    @Test
    void test_isNameExists_whenGivenValidCompanyName_thenReturnTrue() {
        when(companyRepository.findByName(anyString())).thenReturn(companyStore());

        boolean result = companyService.isNameExists(companyDto());

        assertTrue(result);
        verify(companyRepository, times(1)).findByName(companyDto().getName());
    }

    @Test
    void test_isNameExists_whenGivenInvalidCompanyName_thenReturnFalse() {
        when(companyRepository.findByName(anyString())).thenReturn(null);

        boolean result = companyService.isNameExists(companyDto());

        assertFalse(result);
        verify(companyRepository, times(1)).findByName(companyDto().getName());

    }
}