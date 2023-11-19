package com.xyzcorp.api.emissionscalculator.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Objects.nonNull;

@Component
@ConfigurationProperties(prefix = "emissions")
@Setter
public class VehicleEmissionsConfig {

    private static final double DEFAULT_FACTOR = 0.0;

    private Map<String, Double> factor;

    public double get(String vehicleType) {
        double vf = factor.get(vehicleType.toLowerCase());
        return nonNull(vf) ? vf : DEFAULT_FACTOR;
    }

    public Map<String, Double> get() {
        return factor;
    }
}
