package com.lms.project.service.impl;

import com.lms.project.dto.AppUserDTO;
import com.lms.project.dto.AuthDTO;
import com.lms.project.dto.AuthResponseDTO;
import com.lms.project.model.AppUser;
import com.lms.project.model.Role;
import com.lms.project.security.JwtUtil;
import com.lms.project.service.AuthService;
import com.lms.project.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserService userService,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponseDTO registerUser(AppUserDTO appUserDTO) {
        // Check if username exists
        if (userService.findByUsername(appUserDTO.getUsername()).isPresent()) {
            return new AuthResponseDTO(null, "error: Username is already taken.", false);
        }

        // Validate role
        Role role;
        try {
            role = Role.valueOf(appUserDTO.getRole().toUpperCase());
        } catch (Exception e) {
            return new AuthResponseDTO(null, "error: Invalid role. Use STUDENT or INSTRUCTOR.", false);
        }

        // Create new user
        AppUser user = new AppUser();
        user.setFullName(appUserDTO.getFullName());
        user.setUsername(appUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        user.setRole(role);

        userService.saveUser(user);

        // Auto login
        AuthDTO authDTO = new AuthDTO();
        authDTO.setUsername(appUserDTO.getUsername());
        authDTO.setPassword(appUserDTO.getPassword());

        return loginUser(authDTO);
    }

    @Override
    public AuthResponseDTO loginUser(AuthDTO authDTO) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDTO.getUsername(),
                            authDTO.getPassword()
                    )
            );
            AppUser user = userService.findByUsername(authDTO.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate JWT token with username and role
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

            return new AuthResponseDTO(token, "success", true);

        } catch (BadCredentialsException e) {
            return new AuthResponseDTO(null, "error: Invalid username or password.", false);
        }
    }
}
