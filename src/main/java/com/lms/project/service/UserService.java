package com.lms.project.service;

import com.lms.project.model.AppUser;
import java.util.Optional;

public interface UserService {
    AppUser saveUser(AppUser user);                // Save or update a user
    Optional<AppUser> findByUsername(String username); // Find user by username
    Optional<AppUser> findById(Long id);          // Find user by ID
}
