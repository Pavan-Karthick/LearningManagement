package com.lms.project.repository;

import com.lms.project.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Get all courses created by a specific instructor, ordered by start date descending
    Page<Course> findByInstructorIdOrderByStartDateDesc(Long instructorId, Pageable pageable);

    // --- For instructor views, non-paginated ---
    List<Course> findByInstructorIdOrderByStartDateDesc(Long instructorId);

    // Get a course by its ID and instructor ID (for updates/deletes)
    Optional<Course> findByIdAndInstructorId(Long id, Long instructorId);

    // Find all courses for a user by their ID
    List<Course> findByEnrolledUsersId(Long userId);
    Page<Course> findByEnrolledUsersId(Long userId, Pageable pageable);

    Page<Course> findByCategoryAndEnrolledUsersId(String category, Long userId, Pageable pageable);
    // Optional: find all courses by category
    List<Course> findByCategory(String category);
}
