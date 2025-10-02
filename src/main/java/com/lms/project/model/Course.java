package com.lms.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "course",
        indexes = {
                @Index(name = "idx_category", columnList = "category"),
                @Index(name = "idx_start_date", columnList = "startDate"),
                @Index(name = "idx_instructor_id", columnList = "instructor_id")
        })
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String category;
    private double price;
    private LocalDate startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    @JsonBackReference // ignore instructor when serializing course
    private AppUser instructor;

    @ManyToMany
    @JoinTable(
            name = "course_enrollments",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference // ignore enrolledUsers when serializing course
    private List<AppUser> enrolledUsers;
}
