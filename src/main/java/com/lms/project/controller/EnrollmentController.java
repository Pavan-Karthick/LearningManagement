package com.lms.project.controller;

import com.lms.project.model.AppUser;
import com.lms.project.model.Course;
import com.lms.project.service.EnrollmentService;
import com.lms.project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;

    public EnrollmentController(EnrollmentService enrollmentService, UserService userService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
    }

    // Enroll the authenticated user in a course
    @PostMapping("/course/{courseId}")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, Authentication authentication) {
        String username = authentication.getName();
        AppUser user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));;
        boolean enrolled = enrollmentService.enrollUserInCourse(user.getId(), courseId);

        if (enrolled) {
            return ResponseEntity.ok("Enrolled successfully.");
        } else {
            return new ResponseEntity<>("Enrollment failed or already enrolled.", HttpStatus.BAD_REQUEST);
        }
    }

    // Get all courses the authenticated user is enrolled in
    @GetMapping("/my-courses")
    public ResponseEntity<List<Course>> getMyCourses(Authentication authentication) {
        String username = authentication.getName();
        AppUser user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));;
        List<Course> courses = enrollmentService.getEnrolledCourses(user.getId());
        return ResponseEntity.ok(courses);
    }

    // Withdraw the authenticated user from a course
    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<String> withdrawFromCourse(@PathVariable Long courseId, Authentication authentication) {
        String username = authentication.getName();
        AppUser user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));;
        boolean withdrawn = enrollmentService.withdrawFromCourse(user.getId(), courseId);

        if (withdrawn) {
            return ResponseEntity.ok("Successfully withdrawn from course.");
        } else {
            return new ResponseEntity<>("Withdrawal failed or not enrolled.", HttpStatus.BAD_REQUEST);
        }
    }

    // Get all users enrolled in a particular course (for instructors/admin)
    @GetMapping("/course/{courseId}/users")
    public ResponseEntity<List<AppUser>> getUsersByCourse(@PathVariable Long courseId) {
        List<AppUser> users = enrollmentService.getUsersByCourse(courseId);
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }
}
