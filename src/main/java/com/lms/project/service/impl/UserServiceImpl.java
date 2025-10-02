package com.lms.project.service.impl;

import com.lms.project.model.AppUser;
import com.lms.project.repository.UserRepository;
import com.lms.project.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser saveUser(AppUser user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return userRepository.findById(id);
    }
}
