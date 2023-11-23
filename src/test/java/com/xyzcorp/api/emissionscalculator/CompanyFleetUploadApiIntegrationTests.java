package com.xyzcorp.api.emissionscalculator;

import com.xyzcorp.api.emissionscalculator.dto.UserAuthResponseDto;
import com.xyzcorp.api.emissionscalculator.service.UserPublicService;
import com.xyzcorp.api.emissionscalculator.testdata.TestCompanyDataManager;
import com.xyzcorp.api.emissionscalculator.testdata.TestUserDataManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyFleetUploadApiIntegrationTests implements TestUserDataManager, TestCompanyDataManager {

	private static final String EMAIL_ADDRESS = "user@xyz.corp";

    @Autowired
    private MockMvc mvc;
	@Autowired
	private UserPublicService userPublicService;
	@Autowired
	private ResourceLoader resourceLoader;

	private UserAuthResponseDto userAuthResponseDto;

	@BeforeAll
	public void setup() {
		userAuthResponseDto = userPublicService.authenticate(userAuthRequestDto(EXISTING_USER_EMAIL_ADDRESS));
	}

    @Test
    public void test_uploadCompanyFleet_givenValidRequest_thenReturnSuccess() throws Exception {

		mvc.perform(getMockMultipartRequestBuilder(EXISTING_COMPANY_ID).file(getMockMultipartFile())
						.header("Authorization", bearerToken(userAuthResponseDto.getToken())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code", is("S101")))
				.andExpect(jsonPath("$.message", is("Company fleet has been processed successfully.")));

    }

	@Test
	public void test_uploadCompanyFleet_givenInvalidCompanyRequest_thenReturnError() throws Exception {

		mvc.perform(getMockMultipartRequestBuilder("0").file(getMockMultipartFile())
						.header("Authorization", bearerToken(userAuthResponseDto.getToken())))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code", is("E101")))
				.andExpect(jsonPath("$.message", is("Company not found")));

	}

	@Test
	public void test_uploadCompanyFleet_givenInValidFileRequest_thenReturnError() throws Exception {

		mvc.perform(getMockMultipartRequestBuilder(EXISTING_COMPANY_ID).file(getMockInvalidMultipartFile())
						.header("Authorization", bearerToken(userAuthResponseDto.getToken())))
				.andExpect(status().isBadRequest());

	}

	private MockMultipartHttpServletRequestBuilder getMockMultipartRequestBuilder(String companyId) {
		return MockMvcRequestBuilders.multipart("/api/private/v1/company/{companyId}/fleet", companyId);
	}

	private MockMultipartFile getMockMultipartFile() throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = resourceLoader.getResource(COMPANY_FLEET_DATA_SHEET_LOCATION).getInputStream();
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

	private MockMultipartFile getMockInvalidMultipartFile() {
		return new MockMultipartFile("file-invalid",
				"company-fleet",
				"multipart/form-data",
				"This is an invalid file".getBytes());
	}

}
