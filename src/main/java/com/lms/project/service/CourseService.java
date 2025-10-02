package com.lms.project.service;

import com.lms.project.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> getAllCoursesByUser(Long userId); // all courses a student/instructor has

    List<Course> getCoursesByDay(String date, Long userId); // e.g., courses starting on a given day

    List<Course> getCoursesByCategoryAndMonth(String category, String month, Long userId);

    List<String> getAllCourseCategories(Long userId);

    Optional<Course> getCourseById(Long id, Long userId);

    Course addCourse(Course course, Long userId);

    boolean updateCourse(Course course, Long userId);

    boolean deleteCourse(Long id, Long userId);

    List<Course> getCoursesByCategory(String category, Long id);

    Page<Course> getCoursesByUserPaginated(Long userId, Pageable pageable);
    Page<Course> getCoursesByCategoryPaginated(String category, Long userId, Pageable pageable);
    Page<Course> getCoursesByInstructorPaginated(Long instructorId, Pageable pageable);
}
