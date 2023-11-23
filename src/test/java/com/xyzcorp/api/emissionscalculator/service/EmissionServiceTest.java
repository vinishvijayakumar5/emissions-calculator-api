package com.xyzcorp.api.emissionscalculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.dto.EmployeeEmissionDto;
import com.xyzcorp.api.emissionscalculator.dto.VehicleAlternative;
import com.xyzcorp.api.emissionscalculator.exception.DataNotFoundException;
import com.xyzcorp.api.emissionscalculator.testdata.TestCompanyDataManager;
import com.xyzcorp.api.emissionscalculator.utils.VehicleEmissionCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmissionServiceTest implements TestCompanyDataManager {

    @Mock
    private CompanyFleetService companyFleetService;
    @Mock
    private VehicleEmissionCalculator vehicleEmissionCalculator;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private EmissionService emissionService;

    @Test
    void test_getEmissionsByCompany_whenDataFound_calculateEmissions_thenReturnEmissions() {
        when(companyFleetService.get(anyLong())).thenReturn(List.of(companyFleetDto()));
        when(objectMapper.convertValue(any(), eq(EmployeeEmissionDto.class)))
                .thenReturn(employeeEmissionDto());
        when(vehicleEmissionCalculator.get(employeeEmissionDto().getVehicle(), employeeEmissionDto().getMileage()))
                .thenReturn(employeeEmissionDto().getMileage() * EMISSION_FACTOR_OF_CAR);


        List<EmployeeEmissionDto> emissions = emissionService.get("1");

        verifyEmissions(emissions);
    }

    @Test
    void test_getEmissionsByCompany_whenNoDataFound_thenThrowDataNotFoundException() {
        when(companyFleetService.get(anyLong())).thenReturn(List.of());

        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class,
                () -> emissionService.get("1"));

        assertEquals("No emissions data found for the company.", dataNotFoundException.getMessage());
        assertEquals("E107", dataNotFoundException.getContract());
        assertEquals(HttpStatus.NOT_FOUND, dataNotFoundException.getStatus());
    }

    @Test
    void test_getEmissionsByCompanyAndEmployee_whenDataFound_calculateEmissions_thenReturnEmissions() {
        when(companyFleetService.get(anyLong(), anyString())).thenReturn(List.of(companyFleetDto()));
        when(objectMapper.convertValue(any(), eq(EmployeeEmissionDto.class)))
                .thenReturn(employeeEmissionDto());
        when(vehicleEmissionCalculator.get(employeeEmissionDto().getVehicle(), employeeEmissionDto().getMileage()))
                .thenReturn(employeeEmissionDto().getMileage() * EMISSION_FACTOR_OF_CAR);


        List<EmployeeEmissionDto> emissions = emissionService.get(EXISTING_COMPANY_ID, employeeEmissionDto().getEmployeeId());

        verifyEmissions(emissions);
    }

    @Test
    void test_getEmissionsByCompanyAndEmployee_whenNoDataFound_thenThrowDataNotFoundException() {
        when(companyFleetService.get(anyLong(), anyString())).thenReturn(List.of());

        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class,
                () -> emissionService.get(EXISTING_COMPANY_ID, employeeEmissionDto().getEmployeeId()));

        assertEquals(String.format("No emissions data found for the employee id %s", employeeEmissionDto().getEmployeeId())
                , dataNotFoundException.getMessage());
        assertEquals("E106", dataNotFoundException.getContract());
        assertEquals(HttpStatus.NOT_FOUND, dataNotFoundException.getStatus());
    }

    @Test
    void test_getAlternativesByCompanyAndEmployee_whenDataFound_findAlternatives_thenReturnAlternatives() {
        when(companyFleetService.get(anyLong(), anyString())).thenReturn(List.of(companyFleetDto()));
        when(objectMapper.convertValue(any(), eq(EmployeeEmissionDto.class)))
                .thenReturn(employeeEmissionDto());
        when(vehicleEmissionCalculator.get(employeeEmissionDto().getVehicle(), employeeEmissionDto().getMileage()))
                .thenReturn(employeeEmissionDto().getMileage() * EMISSION_FACTOR_OF_CAR);
        when(objectMapper.convertValue(any(), eq(VehicleAlternative.class)))
                .thenReturn(vehicleAlternative());
        when(vehicleEmissionCalculator.getAlternatives(employeeEmissionDto().getVehicle()))
                .thenReturn(vehicleAlternative().getAlternatives());

        List<VehicleAlternative> result = emissionService.getAlternatives(EXISTING_COMPANY_ID, employeeEmissionDto().getEmployeeId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicleAlternative().getVehicle(), result.get(0).getVehicle());
        assertEquals(vehicleAlternative().getMileage(), result.get(0).getMileage());
        assertNotNull(result.get(0).getAlternatives());
        assertEquals(vehicleAlternative().getAlternatives().size(), result.get(0).getAlternatives().size());
        Assertions.assertLinesMatch(vehicleAlternative().getAlternatives(), result.get(0).getAlternatives());
    }

    @Test
    void test_getAlternativesByCompanyAndEmployee_whenNoDataFound_thenThrowDataNotFoundException() {
        when(companyFleetService.get(anyLong(), anyString())).thenReturn(List.of());

        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class,
                () -> emissionService.getAlternatives(EXISTING_COMPANY_ID, employeeEmissionDto().getEmployeeId()));

        assertEquals(String.format("No emissions data found for the employee id %s", employeeEmissionDto().getEmployeeId())
                , dataNotFoundException.getMessage());
        assertEquals("E106", dataNotFoundException.getContract());
        assertEquals(HttpStatus.NOT_FOUND, dataNotFoundException.getStatus());
    }

    private void verifyEmissions(List<EmployeeEmissionDto> emissions) {
        assertNotNull(emissions);
        assertEquals(1, emissions.size());
        assertEquals(employeeEmissionDto().getEmployeeId(), emissions.get(0).getEmployeeId());
        assertEquals(employeeEmissionDto().getVehicle(), emissions.get(0).getVehicle());
        assertEquals(employeeEmissionDto().getMileage(), emissions.get(0).getMileage());
        assertEquals(employeeEmissionDto().getMileage() * EMISSION_FACTOR_OF_CAR,
                emissions.get(0).getEmission());
    }
}