package com.lms.project.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Data
public class AuthDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}