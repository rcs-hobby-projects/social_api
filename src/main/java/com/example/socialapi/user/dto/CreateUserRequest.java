package com.example.socialapi.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "You must input yout name") String name,
        @NotBlank(message = "You must input your email") @Email(message = "Invalid email") String email,
        @NotBlank(message = "You must input password") @Size(min = 6, message = "The password must be 6 charcter or more") String password) {
}
