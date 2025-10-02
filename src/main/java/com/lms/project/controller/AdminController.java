package com.lms.project.controller;

import com.lms.project.model.AppUser;
import com.lms.project.model.Course;
import com.lms.project.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = adminService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    // Get a single user by username
    @GetMapping("/users/{name}")
    public ResponseEntity<AppUser> getUserByName(@PathVariable String name) {
        try {
            AppUser user = adminService.getUserByUsername(name);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = adminService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Promote a user to instructor
    @PutMapping("/users/{id}/promote")
    public ResponseEntity<String> promoteToInstructor(@PathVariable Long id) {
        boolean promoted = adminService.promoteToInstructor(id);
        if (promoted) {
            return ResponseEntity.ok("User promoted to instructor successfully.");
        } else {
            return new ResponseEntity<>("Promotion failed.", HttpStatus.BAD_REQUEST);
        }
    }

    // Get all courses
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = adminService.getAllCourses();
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }

    // Delete a course by ID
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        boolean deleted = adminService.deleteCourse(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
