package com.lms.project.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;


@Data
public class        AppUserDTO {
    @NotBlank
    private String fullName;
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 6)
    private String password;
    private String role; // Optional: STUDENT / INSTRUCTOR
}
