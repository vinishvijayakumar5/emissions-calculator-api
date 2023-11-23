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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyApiIntegrationTests implements TestUserDataManager, TestCompanyDataManager {

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
    public void test_createCompany_givenValidRequest_thenReturnSuccess() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/api/private/v1/company")
                        .content(company())
						.header("Authorization", bearerToken(userAuthResponseDto.getToken()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", greaterThan(0)));
    }

	@Test
	public void test_publish_givenEmptyBody_thenReturnError() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/api/private/v1/company")
						.content("{}")
						.header("Authorization", bearerToken(userAuthResponseDto.getToken()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	public void test_publish_givenValidRequestAndNoToken_thenReturnError() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/api/private/v1/company")
						.content(company())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code", is("AUTH001")));
	}

}
