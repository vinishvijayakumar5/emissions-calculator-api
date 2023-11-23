package com.xyzcorp.api.emissionscalculator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.dto.CompanyFleetDto;
import com.xyzcorp.api.emissionscalculator.entity.CompanyFleet;
import com.xyzcorp.api.emissionscalculator.exception.DataNotFoundException;
import com.xyzcorp.api.emissionscalculator.testdata.TestCompanyDataManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyFleetMapperTest implements TestCompanyDataManager {

    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private CompanyFleetMapper companyFleetMapper;

    @Test
    void test_map_givenCompanyFleetDtoAndCompany_thenReturnCompanyFleet() {
        CompanyFleet expectedFleet = companyFleet();
        when(objectMapper.convertValue(any(), eq(CompanyFleet.class))).thenReturn(expectedFleet);

        CompanyFleet companyFleet = companyFleetMapper.map(companyFleetDto(), companyStore());

        assertNotNull(companyFleet);
        assertEquals(expectedFleet.getCompany().getName(), companyFleet.getCompany().getName());
        assertEquals(expectedFleet.getCompany().getCity(), companyFleet.getCompany().getCity());
        assertEquals(expectedFleet.getCompany().getCountry(), companyFleet.getCompany().getCountry());
        assertEquals(expectedFleet.getCompany().getType(), companyFleet.getCompany().getType());
        assertNotNull(companyFleet.getCompany().getCreatedOn());
        assertEquals(expectedFleet.getEmployeeId(), companyFleet.getEmployeeId());
        assertEquals(expectedFleet.getVehicle(), companyFleet.getVehicle());
        assertEquals(expectedFleet.getMileage(), companyFleet.getMileage());
    }

    @Test
    void test_map_givenCompanyFleetDtoListAndCompany_thenReturnCompanyFleetList() {
        List<CompanyFleet> expectedFleets = List.of(companyFleet());
        when(objectMapper.convertValue(any(), eq(CompanyFleet.class))).thenReturn(expectedFleets.get(0));

        List<CompanyFleet> companyFleets = companyFleetMapper.map(List.of(companyFleetDto()), companyStore());

        assertNotNull(companyFleets);
        assertEquals(1, companyFleets.size());
        assertEquals(expectedFleets.get(0).getCompany().getName(), companyFleets.get(0).getCompany().getName());
        assertEquals(expectedFleets.get(0).getCompany().getCity(), companyFleets.get(0).getCompany().getCity());
        assertEquals(expectedFleets.get(0).getCompany().getCountry(), companyFleets.get(0).getCompany().getCountry());
        assertEquals(expectedFleets.get(0).getCompany().getType(), companyFleets.get(0).getCompany().getType());
        assertNotNull(companyFleets.get(0).getCompany().getCreatedOn());
        assertEquals(expectedFleets.get(0).getEmployeeId(), companyFleets.get(0).getEmployeeId());
        assertEquals(expectedFleets.get(0).getVehicle(), companyFleets.get(0).getVehicle());
        assertEquals(expectedFleets.get(0).getMileage(), companyFleets.get(0).getMileage());
    }

    @Test
    void test_map_givenCompanyFleetList_thenReturnCompanyFleetDtoList() {
        List<CompanyFleetDto> expectedFleetDtoList = List.of(companyFleetDto());
        when(objectMapper.convertValue(any(), eq(CompanyFleetDto.class))).thenReturn(expectedFleetDtoList.get(0));

        List<CompanyFleetDto> companyFleetDtoList = companyFleetMapper.map(List.of(companyFleet()));

        assertNotNull(companyFleetDtoList);
        assertEquals(1, companyFleetDtoList.size());
        assertEquals(expectedFleetDtoList.get(0).getEmployeeId(), companyFleetDtoList.get(0).getEmployeeId());
        assertEquals(expectedFleetDtoList.get(0).getVehicle(), companyFleetDtoList.get(0).getVehicle());
        assertEquals(expectedFleetDtoList.get(0).getMileage(), companyFleetDtoList.get(0).getMileage());
    }

    @Test
    void test_map_givenCompanyFleet_thenReturnCompanyFleetDto() {
        CompanyFleetDto expectedCompanyFleetDto = companyFleetDto();
        when(objectMapper.convertValue(any(), eq(CompanyFleetDto.class))).thenReturn(expectedCompanyFleetDto);

        CompanyFleetDto companyFleetDto = companyFleetMapper.map(companyFleet());

        assertNotNull(companyFleetDto);
        assertEquals(expectedCompanyFleetDto.getEmployeeId(), companyFleetDto.getEmployeeId());
        assertEquals(expectedCompanyFleetDto.getVehicle(), companyFleetDto.getVehicle());
        assertEquals(expectedCompanyFleetDto.getMileage(), companyFleetDto.getMileage());
    }

    @Test
    void test_map_givenOptionalCompanyFleet_thenReturnCompanyFleetDto() {
        CompanyFleetDto expectedCompanyFleetDto = companyFleetDto();
        when(objectMapper.convertValue(any(), eq(CompanyFleetDto.class))).thenReturn(expectedCompanyFleetDto);

        CompanyFleetDto companyFleetDto = companyFleetMapper.map(Optional.of(companyFleet()));

        assertNotNull(companyFleetDto);
        assertEquals(expectedCompanyFleetDto.getEmployeeId(), companyFleetDto.getEmployeeId());
        assertEquals(expectedCompanyFleetDto.getVehicle(), companyFleetDto.getVehicle());
        assertEquals(expectedCompanyFleetDto.getMileage(), companyFleetDto.getMileage());
    }

    @Test
    void test_map_givenEmptyOptionalCompanyFleet_thenThrowDataNotFoundException() {

        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class,
                () -> companyFleetMapper.map(Optional.empty()));

        assertEquals("Company's fleet not found", dataNotFoundException.getMessage());
        assertEquals("E103", dataNotFoundException.getContract());
        assertEquals(HttpStatus.NOT_FOUND, dataNotFoundException.getStatus());
    }
}