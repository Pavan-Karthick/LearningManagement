package com.lms.project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "app_user",
        indexes = {
                @Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_role", columnList = "role")
        })
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // STUDENT / INSTRUCTOR / ADMIN

    // Courses created by the user (if instructor)
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // serialize normally
    private List<Course> createdCourses;

    // Courses the user is enrolled in
    @ManyToMany(mappedBy = "enrolledUsers")
    @JsonManagedReference // serialize normally
    private List<Course> enrolledCourses;
}
