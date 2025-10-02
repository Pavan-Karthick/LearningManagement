package com.lms.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String message;
    private boolean success; // Lombok will generate isSuccess() automatically

}