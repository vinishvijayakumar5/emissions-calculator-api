package com.xyzcorp.api.emissionscalculator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzcorp.api.emissionscalculator.testdata.TestUserDataManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserPublicApiIntegrationTests implements TestUserDataManager {

	private static final String EMAIL_ADDRESS = "admin_test_one@xyz.corp";

    @Autowired
    private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;

    @Test
    public void test_registerAndAuthenticateUser_givenValidRequest_thenReturnSuccess() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/api/public/v1/user/register")
                        .content(userToRegister())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", greaterThan(0)));

		mvc.perform(MockMvcRequestBuilders.post("/api/public/v1/user/authenticate")
						.content(userToAuthenticate())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token", notNullValue()));
    }

    private String userToRegister() throws JsonProcessingException {
		return objectMapper.writeValueAsString(userDto(EMAIL_ADDRESS));
    }

	private String userToAuthenticate() throws JsonProcessingException {
		return objectMapper.writeValueAsString(userAuthRequestDto(EMAIL_ADDRESS));
	}
}
