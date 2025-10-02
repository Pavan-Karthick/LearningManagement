package com.lms.project.service;

import com.lms.project.model.AppUser;
import com.lms.project.model.Course;
import com.lms.project.model.Role;

import java.util.List;

public interface AdminService {

    List<AppUser> getAllUsers();

    AppUser getUserByUsername(String username);

    boolean deleteUser(Long id);

    boolean promoteToInstructor(Long id);

    List<Course> getAllCourses();

    boolean deleteCourse(Long id);

    List<AppUser> getUsersByRole(Role role);

    AppUser updateUser(AppUser user);
}
