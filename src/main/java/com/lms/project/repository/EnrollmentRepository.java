package com.lms.project.repository;

import com.lms.project.model.Course;
import com.lms.project.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Course, Long> {

    // Find all courses a student is enrolled in
    List<Course> findByEnrolledUsersId(Long userId);

    // Find all students enrolled in a course
    List<AppUser> findEnrolledUsersById(Long courseId);
}
