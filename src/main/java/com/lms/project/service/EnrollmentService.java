package com.lms.project.service;

import com.lms.project.model.AppUser;
import com.lms.project.model.Course;

import java.util.List;

public interface EnrollmentService {
    boolean enrollUserInCourse(Long userId, Long courseId);
    List<Course> getEnrolledCourses(Long userId);
    boolean withdrawFromCourse(Long userId, Long courseId);
    List<AppUser> getUsersByCourse(Long courseId);
}
