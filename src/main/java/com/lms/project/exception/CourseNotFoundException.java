package com.lms.project.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long id) {
        super("Course with ID " + id + " not found.");
    }
}
