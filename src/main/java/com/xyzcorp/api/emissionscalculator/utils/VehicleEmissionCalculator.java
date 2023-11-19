package com.xyzcorp.api.emissionscalculator.utils;

import com.xyzcorp.api.emissionscalculator.config.VehicleEmissionsConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class VehicleEmissionCalculator {

    private VehicleEmissionsConfig vehicleEmissionsConfig;

    /**
     * This method gets the factor from config.
     * This implementation has to be modified if there is an external provider who provides emissions calculation.
     * @param vehicleType
     * @param mileage
     * @return
     */
    public double get(String vehicleType, Double mileage) {
        return vehicleEmissionsConfig.get(vehicleType) * mileage;
    }

    /**
     * This method filter the vehicles which has lower factor than the current vehicle.
     * This implementation has to be modified if there is an external provider who provides alternative vehicles.
     * @param vehicleType
     * @return
     */
    public List<String> getAlternatives(String vehicleType) {
        Double factor = vehicleEmissionsConfig.get(vehicleType);
        Map<String, Double> factors = vehicleEmissionsConfig.get();
        return factors.entrySet().stream()
                .filter(f -> f.getValue() < factor)
                .map(f -> f.getKey())
                .collect(Collectors.toUnmodifiableList());
    }
}
