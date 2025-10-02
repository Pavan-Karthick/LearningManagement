package com.lms.project.controller;

import com.lms.project.dto.AppUserDTO;
import com.lms.project.dto.AuthDTO;
import com.lms.project.dto.AuthResponseDTO;
import com.lms.project.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@RestController
@CrossOrigin(origins = "*")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // User signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody AppUserDTO appUserDTO) {

            appUserDTO.setRole("STUDENT");

        AuthResponseDTO response = authService.registerUser(appUserDTO);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

    // User login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthDTO authDTO) {
        AuthResponseDTO response = authService.loginUser(authDTO);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        }
}