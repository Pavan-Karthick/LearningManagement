package com.lms.project.service.impl;

import com.lms.project.model.AppUser;
import com.lms.project.model.Course;
import com.lms.project.model.Role;
import com.lms.project.repository.UserRepository;
import com.lms.project.repository.CourseRepository;
import com.lms.project.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public  AdminServiceImpl(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean promoteToInstructor(Long id) {
        AppUser user = userRepository.findById(id).orElse(null);
        if (user == null) return false;
        user.setRole(Role.INSTRUCTOR);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public boolean deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) return false;
        courseRepository.deleteById(id);
        return true;
    }

    @Override
    public List<AppUser> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public AppUser updateUser(AppUser user) {
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("User not found with id: " + user.getId());
        }
        return userRepository.save(user);
    }
}
