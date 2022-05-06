package com.example.dailyplanner.security.payload.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@Getter
public class SignupRequest {
    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    private Set<String> roles;

    @NotBlank
    @Size(min = 5, max = 40)
    private String password;
}