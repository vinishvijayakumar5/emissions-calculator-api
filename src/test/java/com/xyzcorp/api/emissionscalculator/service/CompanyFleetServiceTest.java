package com.xyzcorp.api.emissionscalculator.service;

import com.xyzcorp.api.emissionscalculator.dto.CompanyFleetDto;
import com.xyzcorp.api.emissionscalculator.dto.GenericResponseDto;
import com.xyzcorp.api.emissionscalculator.exception.InvalidRequestException;
import com.xyzcorp.api.emissionscalculator.mapper.CompanyFleetMapper;
import com.xyzcorp.api.emissionscalculator.repository.CompanyFleetRepository;
import com.xyzcorp.api.emissionscalculator.testdata.TestCompanyDataManager;
import com.xyzcorp.api.emissionscalculator.utils.FleetFileOperations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyFleetServiceTest implements TestCompanyDataManager {

    @Mock
    private CompanyService companyService;
    @Mock
    private CompanyFleetRepository companyFleetRepository;
    @Mock
    private CompanyFleetMapper companyFleetMapper;
    @Mock
    private FleetFileOperations fleetFileOperations;
    @InjectMocks
    private CompanyFleetService companyFleetService;

    @Test
    void test_persist_whenGivenValidData_thenReturnSuccess() throws IOException {
        when(companyService.getCompany(anyLong())).thenReturn(companyStore());
        when(fleetFileOperations.parse(any())).thenReturn(List.of(companyFleetDto()));
        when(companyFleetMapper.map(anyList(), any())).thenReturn(List.of(companyFleet()));
        when(companyFleetRepository.saveAll(anyList())).thenReturn(List.of(companyFleet()));

        ResponseEntity responseEntity = companyFleetService.persist(1, getMockMultipartFile());

        assertNotNull(responseEntity);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(responseEntity.hasBody());

        var body = (GenericResponseDto) responseEntity.getBody();

        assertEquals(true, body.success());
        assertEquals("S101", body.code());
        assertEquals("Company fleet has been processed successfully.", body.message());
    }

    @Test
    void test_persist_whenGivenInvalidCompanyId_thenThrowInvalidRequestException() throws IOException {
        when(companyService.getCompany(anyLong())).thenReturn(null);

        InvalidRequestException invalidRequestException = assertThrows(InvalidRequestException.class,
                () -> companyFleetService.persist(0, getMockMultipartFile()));

        assertEquals("Invalid request, either company or file not found.", invalidRequestException.getMessage());
        assertEquals("E102", invalidRequestException.getContract());
        assertEquals(HttpStatus.BAD_REQUEST, invalidRequestException.getStatus());
    }

    @Test
    void test_persist_whenGivenInvalidFile_thenThrowInvalidRequestException() throws IOException {
        when(companyService.getCompany(anyLong())).thenReturn(companyStore());

        InvalidRequestException invalidRequestException = assertThrows(InvalidRequestException.class,
                () -> companyFleetService.persist(1, null));

        assertEquals("Invalid request, either company or file not found.", invalidRequestException.getMessage());
        assertEquals("E102", invalidRequestException.getContract());
        assertEquals(HttpStatus.BAD_REQUEST, invalidRequestException.getStatus());
    }

    @Test
    void test_getFleetByCompanyIdAndEmployeeId_whenGivenValidData_thenReturnFleets() {
        when(companyFleetRepository.findByCompanyIdAndEmployeeId(anyLong(), anyString())).thenReturn(List.of(companyFleet()));
        when(companyFleetMapper.map(anyList())).thenReturn(List.of(companyFleetDto()));

        List<CompanyFleetDto> fleets = companyFleetService.get(1, employeeEmissionDto().getEmployeeId());

        verifyFleets(fleets);
    }

    @Test
    void test_getFleetByCompanyIdAndEmployeeId_whenGivenInvalidData_thenReturnEmptyResults() {
        when(companyFleetRepository.findByCompanyIdAndEmployeeId(anyLong(), anyString())).thenReturn(List.of());

        List<CompanyFleetDto> fleets = companyFleetService.get(1, employeeEmissionDto().getEmployeeId());

        assertNotNull(fleets);
        assertEquals(0, fleets.size());
    }

    @Test
    void test_getFleetByCompanyId_whenGivenValidCompanyId_thenReturnFleets() {
        when(companyFleetRepository.findByCompanyId(anyLong())).thenReturn(List.of(companyFleet()));
        when(companyFleetMapper.map(anyList())).thenReturn(List.of(companyFleetDto()));

        List<CompanyFleetDto> fleets = companyFleetService.get(1);

        verifyFleets(fleets);
    }

    @Test
    void test_getFleetByCompanyId_whenGivenInalidCompanyId_thenReturnEmptyResults() {
        when(companyFleetRepository.findByCompanyId(anyLong())).thenReturn(List.of());

        List<CompanyFleetDto> fleets = companyFleetService.get(1);

        assertNotNull(fleets);
        assertEquals(0, fleets.size());
    }

    private void verifyFleets(List<CompanyFleetDto> fleets) {
        assertNotNull(fleets);
        assertEquals(1, fleets.size());
        assertEquals(companyFleetDto().getEmployeeId(), fleets.get(0).getEmployeeId());
        assertEquals(companyFleetDto().getVehicle(), fleets.get(0).getVehicle());
        assertEquals(companyFleetDto().getMileage(), fleets.get(0).getMileage());
    }

    private MockMultipartFile getMockMultipartFile() throws IOException {
        InputStream inputStream = null;
        try {
            FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();
            inputStream = fileSystemResourceLoader.getResource(COMPANY_FLEET_DATA_SHEET_LOCATION).getInputStream();;
            return new MockMultipartFile("file",
                    "company-fleet",
                    "multipart/form-data",
                    inputStream);
        } finally {
            if(Objects.nonNull(inputStream)) {
                inputStream.close();
            }
        }
    }
}