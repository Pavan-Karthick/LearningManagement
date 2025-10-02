package com.lms.project.service.impl;

import com.lms.project.model.AppUser;
import com.lms.project.model.Course;
import com.lms.project.repository.CourseRepository;
import com.lms.project.service.CourseService;
import com.lms.project.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserService userService;

    public CourseServiceImpl(CourseRepository courseRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
    }

    @Override
    public List<Course> getAllCoursesByUser(Long userId) {
        return new ArrayList<>(courseRepository.findByEnrolledUsersId(userId));
    }

    @Override
    public List<Course> getCoursesByDay(String date, Long userId) {
        List<Course> courses = courseRepository.findByEnrolledUsersId(userId);
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
            if (course.getStartDate().toString().equals(date)) {
                result.add(course);
            }
        }
        return result;
    }

    @Override
    public List<Course> getCoursesByCategory(String category, Long userId) {
        List<Course> courses = courseRepository.findByEnrolledUsersId(userId);
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
            if (course.getCategory().equalsIgnoreCase(category)) {
                result.add(course);
            }
        }
        return result;
    }


    @Override
    public List<Course> getCoursesByCategoryAndMonth(String category, String month, Long userId) {
        List<Course> courses = courseRepository.findByEnrolledUsersId(userId);
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
            if (course.getCategory().equalsIgnoreCase(category)
                    && course.getStartDate().toString().startsWith(month)) {
                result.add(course);
            }
        }
        return result;
    }

    @Override
    public List<String> getAllCourseCategories(Long userId) {
        List<Course> courses = courseRepository.findByEnrolledUsersId(userId);
        List<String> categories = new ArrayList<>();
        for (Course course : courses) {
            if (!categories.contains(course.getCategory())) {
                categories.add(course.getCategory());
            }
        }
        return categories;
    }

    @Override
    public Optional<Course> getCourseById(Long id, Long userId) {
        return courseRepository.findById(id)
                .filter(course -> course.getEnrolledUsers().stream().anyMatch(u -> u.getId().equals(userId)));
    }


    @Override
    public Course addCourse(Course course, Long instructorId) {
        AppUser instructor = userService.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }


    @Override
    public boolean updateCourse(Course updatedCourse, Long instructorId) {
        return courseRepository.findById(updatedCourse.getId())
                .filter(course -> course.getInstructor().getId().equals(instructorId))
                .map(course -> {
                    updatedCourse.setInstructor(course.getInstructor());
                    courseRepository.save(updatedCourse);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean deleteCourse(Long id, Long instructorId) {
        return courseRepository.findById(id)
                .filter(course -> course.getInstructor().getId().equals(instructorId))
                .map(course -> {
                    courseRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Page<Course> getCoursesByUserPaginated(Long userId, Pageable pageable) {
        return courseRepository.findByEnrolledUsersId(userId, pageable);
    }

    @Override
    public Page<Course> getCoursesByCategoryPaginated(String category, Long userId, Pageable pageable) {
        return courseRepository.findByCategoryAndEnrolledUsersId(category, userId, pageable);
    }

    @Override
    public Page<Course> getCoursesByInstructorPaginated(Long instructorId, Pageable pageable) {
        return courseRepository.findByInstructorIdOrderByStartDateDesc(instructorId, pageable);
    }
}
