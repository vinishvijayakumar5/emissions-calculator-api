package com.xyzcorp.api.emissionscalculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.dto.CompanyFleetDto;
import com.xyzcorp.api.emissionscalculator.dto.EmployeeEmissionDto;
import com.xyzcorp.api.emissionscalculator.dto.VehicleAlternative;
import com.xyzcorp.api.emissionscalculator.exception.DataNotFoundException;
import com.xyzcorp.api.emissionscalculator.utils.VehicleEmissionCalculator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@AllArgsConstructor
public class EmissionService {

    private CompanyFleetService companyFleetService;
    private VehicleEmissionCalculator vehicleEmissionCalculator;
    private ObjectMapper objectMapper;

    public List<EmployeeEmissionDto> get(String companyId) {
        List<CompanyFleetDto> fleets = companyFleetService.get(Long.valueOf(companyId));
        if(isNotEmpty(fleets)) {
            return fleets.parallelStream()
                    .map(this::map)
                    .collect(Collectors.toUnmodifiableList());
        }

        throw new DataNotFoundException("No emissions data found for the company.", "E107", HttpStatus.NOT_FOUND);
    }

    public List<EmployeeEmissionDto> get(String companyId, String employeeId) {
        List<CompanyFleetDto> fleets = companyFleetService.get(Long.valueOf(companyId), employeeId);
        if(isNotEmpty(fleets)) {
            return fleets.stream()
                    .map(this::map)
                    .collect(Collectors.toUnmodifiableList());
        }

        throw new DataNotFoundException(format("No emissions data found for the employee id %s", employeeId),
                "E106", HttpStatus.NOT_FOUND);
    }

    public List<VehicleAlternative> getAlternatives(String companyId, String employeeId) {
        List<EmployeeEmissionDto> emissions = get(companyId, employeeId);
        return emissions.stream()
                .map(this::mapAlternatives)
                .collect(Collectors.toUnmodifiableList());
    }

    private EmployeeEmissionDto map(CompanyFleetDto fleet) {
        EmployeeEmissionDto emission = objectMapper.convertValue(fleet, EmployeeEmissionDto.class);
        emission.setEmission(getEmission(fleet));
        return emission;
    }

    private VehicleAlternative mapAlternatives(EmployeeEmissionDto emission) {
        VehicleAlternative vehicleAlternative = objectMapper.convertValue(emission, VehicleAlternative.class);
        vehicleAlternative.setAlternatives(vehicleEmissionCalculator.getAlternatives(emission.getVehicle()));
        return vehicleAlternative;
    }

    private double getEmission(CompanyFleetDto fleet) {
        return vehicleEmissionCalculator.get(fleet.getVehicle(), fleet.getMileage());
    }
}
