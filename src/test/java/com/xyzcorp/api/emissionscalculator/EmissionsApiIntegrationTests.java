package com.xyzcorp.api.emissionscalculator;

import com.xyzcorp.api.emissionscalculator.dto.UserAuthResponseDto;
import com.xyzcorp.api.emissionscalculator.service.UserPublicService;
import com.xyzcorp.api.emissionscalculator.testdata.TestCompanyDataManager;
import com.xyzcorp.api.emissionscalculator.testdata.TestUserDataManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmissionsApiIntegrationTests implements TestUserDataManager, TestCompanyDataManager {

    @Autowired
    private MockMvc mvc;
	@Autowired
	private UserPublicService userPublicService;

	private UserAuthResponseDto userAuthResponseDto;

	@BeforeAll
	public void setup() {
		userAuthResponseDto = userPublicService.authenticate(userAuthRequestDto(EXISTING_USER_EMAIL_ADDRESS));
	}

    @Test
    public void test_getEmissions_givenValidRequest_thenReturnSuccess() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/private/v1/company/{companyId}/emission", EXISTING_COMPANY_ID)
						.header("Authorization", bearerToken(userAuthResponseDto.getToken()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].employeeId", is(employeeEmissionDto().getEmployeeId())))
				.andExpect(jsonPath("$[0].vehicle", is(employeeEmissionDto().getVehicle())))
				.andExpect(jsonPath("$[0].mileage", is(employeeEmissionDto().getMileage())))
				.andExpect(jsonPath("$[0].emission", is(employeeEmissionDto().getMileage() * EMISSION_FACTOR_OF_CAR)));

    }

	@Test
	public void test_getEmissions_givenInvalidCompanyRequest_thenReturnError() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/private/v1/company/{companyId}/emission", "0")
						.header("Authorization", bearerToken(userAuthResponseDto.getToken()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code", is("E107")))
				.andExpect(jsonPath("$.message", is("No emissions data found for the company.")));

	}

	@Test
	public void test_getEmissionsByEmployee_givenValidRequest_thenReturnSuccess() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/private/v1/company/{companyId}/emission/employee/{employeeId}",
								EXISTING_COMPANY_ID, employeeEmissionDto().getEmployeeId())
						.header("Authorization", bearerToken(userAuthResponseDto.getToken()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].employeeId", is(employeeEmissionDto().getEmployeeId())))
				.andExpect(jsonPath("$[0].vehicle", is(employeeEmissionDto().getVehicle())))
				.andExpect(jsonPath("$[0].mileage", is(employeeEmissionDto().getMileage())))
				.andExpect(jsonPath("$[0].emission", is(employeeEmissionDto().getMileage() * EMISSION_FACTOR_OF_CAR)));

	}

	@Test
	public void test_getEmissionsByEmployee_givenInvalidRequest_thenReturnSuccess() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/private/v1/company/{companyId}/emission/employee/{employeeId}",
								"0", employeeEmissionDto().getEmployeeId())
						.header("Authorization", bearerToken(userAuthResponseDto.getToken()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code", is("E106")))
				.andExpect(jsonPath("$.message",
						is(String.format("No emissions data found for the employee id %s",
								employeeEmissionDto().getEmployeeId()))));

	}

	@Test
	public void test_getAlternativeVehiclesByEmployee_givenValidRequest_thenReturnSuccess() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/private/v1/company/{companyId}/emission/employee/{employeeId}/alternatives",
								EXISTING_COMPANY_ID, employeeEmissionDto().getEmployeeId())
						.header("Authorization", bearerToken(userAuthResponseDto.getToken()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].vehicle", is(vehicleAlternative().getVehicle())))
				.andExpect(jsonPath("$[0].mileage", is(vehicleAlternative().getMileage())))
				.andExpect(jsonPath("$[0].emission", is(vehicleAlternative().getMileage() * EMISSION_FACTOR_OF_CAR)))
				.andExpect(jsonPath("$[0].alternatives", Matchers.hasSize(vehicleAlternative().getAlternatives().size())))
				.andExpect(jsonPath("$[0].alternatives", Matchers.hasItems(vehicleAlternative().getAlternatives().stream().findAny().get())));

	}

	@Test
	public void test_getAlternativeVehiclesByEmployee_givenInvalidRequest_thenReturnError() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/private/v1/company/{companyId}/emission/employee/{employeeId}/alternatives",
								EXISTING_COMPANY_ID, "1111")
						.header("Authorization", bearerToken(userAuthResponseDto.getToken()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code", is("E106")))
				.andExpect(jsonPath("$.message",
						is(String.format("No emissions data found for the employee id %s", "1111"))));

	}


}
