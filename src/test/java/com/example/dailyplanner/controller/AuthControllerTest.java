package com.example.dailyplanner.controller;

import com.example.dailyplanner.model.Role;
import com.example.dailyplanner.model.UserRole;
import com.example.dailyplanner.repository.RoleRepository;
import com.example.dailyplanner.security.payload.request.LoginRequest;
import com.example.dailyplanner.security.payload.request.SignupRequest;
import com.example.dailyplanner.security.payload.response.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static com.example.dailyplanner.TestUtils.convertJSONStringToObject;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void signupAndSignin() throws Exception {
        Set<String> roles = new HashSet<>();
        roleRepository.save(new Role(UserRole.ROLE_USER));
        roles.add("ROLE_USER");
        SignupRequest signupRequest = SignupRequest.builder().username("admin").password("setup").roles(roles).build();
        final String expectedResponseContent = objectMapper.writeValueAsString(signupRequest);
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup").content(expectedResponseContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        LoginRequest loginRequest = LoginRequest.builder()
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword()).build();
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(signinResponse -> {
                    String json = signinResponse.getResponse().getContentAsString();
                    JwtResponse jwtResponse = (JwtResponse) convertJSONStringToObject(json, JwtResponse.class);
                    mvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                                    .header("Authorization", String.format("Bearer %s", jwtResponse.getAccessToken()))
                                    .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());
                });
        ;
    }
}
