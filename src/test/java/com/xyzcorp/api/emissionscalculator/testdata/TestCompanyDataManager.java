package com.xyzcorp.api.emissionscalculator.testdata;

import com.xyzcorp.api.emissionscalculator.dto.CompanyDto;
import com.xyzcorp.api.emissionscalculator.dto.CompanyFleetDto;
import com.xyzcorp.api.emissionscalculator.dto.EmployeeEmissionDto;
import com.xyzcorp.api.emissionscalculator.dto.VehicleAlternative;
import com.xyzcorp.api.emissionscalculator.entity.Company;
import com.xyzcorp.api.emissionscalculator.entity.CompanyFleet;

import java.time.LocalDateTime;
import java.util.List;

public interface TestCompanyDataManager {

	String EXISTING_COMPANY_ID = "1";
	double EMISSION_FACTOR_OF_CAR = 0.2;
	String COMPANY_FLEET_DATA_SHEET_LOCATION = "classpath:company-fleet.xlsx";

    default String company() {
        return """
				{
				     "name":"CITI",
				     "type":"banking",
				     "city":"london",
				     "country": "UK"
				 }
				""";
    }

	default CompanyDto companyDto() {
		return new CompanyDto("NETFLIX", "entertainment", "NJ", "US");
	}

	default Company companyStore() {
		return new Company(1, "NETFLIX", "entertainment", "NJ", "US", LocalDateTime.now());
	}

	default EmployeeEmissionDto employeeEmissionDto() {
		return EmployeeEmissionDto.builder()
				.employeeId("12345")
				.vehicle("Car")
				.mileage(450.45)
				.build();
	}

	default VehicleAlternative vehicleAlternative() {
		return VehicleAlternative.builder()
				.vehicle("Car")
				.mileage(450.45)
				.alternatives(List.of("rail", "train", "bus", "air", "bicycle", "electric-bicycle", "electric-scooter", "walking"))
				.build();
	}

	default CompanyFleetDto companyFleetDto() {
		return CompanyFleetDto.builder()
				.employeeId("12345")
				.vehicle("Car")
				.mileage(450.45)
				.build();
	}

	default CompanyFleet companyFleet() {
		return new CompanyFleet(1, "12345", "Car", 450.45, companyStore(), LocalDateTime.now());
	}
}
