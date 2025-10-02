package com.lms.project.controller;

import com.lms.project.model.AppUser;
import com.lms.project.model.Course;
import com.lms.project.service.CourseService;
import com.lms.project.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    private Optional<AppUser> getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Course>> getCoursesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        Optional<AppUser> userOpt = getAuthenticatedUser(authentication);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursesPage = courseService.getCoursesByUserPaginated(userOpt.get().getId(), pageable);
        return ResponseEntity.ok(coursesPage);
    }

    // --- Paginated endpoint by category ---
    @GetMapping("/category/{category}/paginated")
    public ResponseEntity<Page<Course>> getCoursesByCategoryPaginated(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        Optional<AppUser> userOpt = getAuthenticatedUser(authentication);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursesPage = courseService.getCoursesByCategoryPaginated(category, userOpt.get().getId(), pageable);
        return ResponseEntity.ok(coursesPage);
    }

    // Get all courses for the authenticated user
    @GetMapping
    public ResponseEntity<List<Course>> getCourses(Authentication authentication) {
        Optional<AppUser> optUser = getAuthenticatedUser(authentication);
        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Course> courses = courseService.getAllCoursesByUser(optUser.get().getId());
        return ResponseEntity.ok(courses);
    }
    // Get courses by category for the authenticated user
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Course>> getCoursesByCategory(@PathVariable String category, Authentication authentication) {
        Optional<AppUser> optUser = getAuthenticatedUser(authentication);
        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Course> courses = courseService.getCoursesByCategory(category, optUser.get().getId());
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(courses);
    }


    // Get all course categories for the authenticated user
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCourseCategories(Authentication authentication) {
        Optional<AppUser> optUser = getAuthenticatedUser(authentication);
        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<String> categories = courseService.getAllCourseCategories(optUser.get().getId());
        return ResponseEntity.ok(categories);
    }

    // Create a new course
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course, Authentication authentication) {
        return getAuthenticatedUser(authentication)
                .map(user -> ResponseEntity.ok(courseService.addCourse(course, user.getId())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // Update an existing course
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails, Authentication authentication) {
        Optional<AppUser> optUser = getAuthenticatedUser(authentication);
        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        courseDetails.setId(id);
        boolean updated = courseService.updateCourse(courseDetails, optUser.get().getId());

        return updated ? ResponseEntity.ok(courseDetails) : ResponseEntity.notFound().build();
    }


    // Delete a course
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id, Authentication authentication) {
        Optional<AppUser> optUser = getAuthenticatedUser(authentication);
        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean deleted = courseService.deleteCourse(id, optUser.get().getId());
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


}
