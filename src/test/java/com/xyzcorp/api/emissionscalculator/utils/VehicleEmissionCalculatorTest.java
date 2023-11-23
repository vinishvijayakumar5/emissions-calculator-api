package com.xyzcorp.api.emissionscalculator.utils;

import com.xyzcorp.api.emissionscalculator.config.VehicleEmissionsConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleEmissionCalculatorTest {

    @Mock
    private VehicleEmissionsConfig vehicleEmissionsConfig;
    @InjectMocks
    private VehicleEmissionCalculator vehicleEmissionCalculator;

    @Test
    void test_getEmission_givenValidVehicleTypeAndMileage_thenReturnEmission() {
        when(vehicleEmissionsConfig.get(anyString())).thenReturn(0.2);

        double emission = vehicleEmissionCalculator.get("CAR", 50.55);
        assertEquals(0.2 * 50.55, emission);

        emission = vehicleEmissionCalculator.get("car", 50.55);
        assertEquals(0.2 * 50.55, emission);

        emission = vehicleEmissionCalculator.get("Car", 50.55);
        assertEquals(0.2 * 50.55, emission);

        emission = vehicleEmissionCalculator.get("car", 0.0);
        assertEquals(0.0, emission);
    }

    @Test
    void test_getEmission_givenInvalidVehicleTypeAndValidMileage_thenReturnEmission() {
        when(vehicleEmissionsConfig.get(anyString())).thenReturn(0.0);

        double emission = vehicleEmissionCalculator.get("test", 50.55);

        assertEquals(0.0, emission);
    }

    @Test
    void test_getAlternatives_givenValidVehicleType_thenReturnAlternatives() {
        List<String> alternativesExpected = List.of("bus", "train", "bicycle");
        when(vehicleEmissionsConfig.get(anyString())).thenReturn(0.2);
        when(vehicleEmissionsConfig.get()).thenReturn(Map.of("car", 0.2, "bus", 0.1, "train", 0.05, "bicycle", 0.0));

        List<String> alternatives = vehicleEmissionCalculator.getAlternatives("car");

        assertNotNull(alternatives);
        assertEquals(3, alternatives.size());
        assertTrue(alternatives.stream().allMatch(a -> alternativesExpected.contains(a)));
    }

    @Test
    void test_getAlternatives_givenInvalidVehicleType_thenReturnNoAlternatives() {
        when(vehicleEmissionsConfig.get(anyString())).thenReturn(0.0);
        when(vehicleEmissionsConfig.get()).thenReturn(Map.of("car", 0.2, "bus", 0.1, "train", 0.05, "bicycle", 0.0));

        List<String> alternatives = vehicleEmissionCalculator.getAlternatives("test");

        assertNotNull(alternatives);
        assertEquals(0, alternatives.size());
    }
}