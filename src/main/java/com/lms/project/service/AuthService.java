package com.lms.project.service;

import com.lms.project.dto.AppUserDTO;
import com.lms.project.dto.AuthDTO;
import com.lms.project.dto.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO registerUser(AppUserDTO appUserDTO);
    AuthResponseDTO loginUser(AuthDTO authDTO);
}
