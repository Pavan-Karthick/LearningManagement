package com.lms.project.service.impl;

import com.lms.project.model.AppUser;
import com.lms.project.model.Course;
import com.lms.project.repository.CourseRepository;
import com.lms.project.repository.UserRepository;
import com.lms.project.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public boolean enrollUserInCourse(Long userId, Long courseId) {
        AppUser user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        if (course.getEnrolledUsers().contains(user)) {
            return false; // already enrolled
        }
        course.getEnrolledUsers().add(user);
        courseRepository.save(course);
        return true;
    }

    @Override
    public List<Course> getEnrolledCourses(Long userId) {
        AppUser user = userRepository.findById(userId).orElseThrow();
        return user.getEnrolledCourses();
    }

    @Override
    public boolean withdrawFromCourse(Long userId, Long courseId) {
        AppUser user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        if (!course.getEnrolledUsers().contains(user)) {
            return false; // not enrolled
        }
        course.getEnrolledUsers().remove(user);
        courseRepository.save(course);
        return true;
    }

    @Override
    public List<AppUser> getUsersByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        return course.getEnrolledUsers();
    }
}
