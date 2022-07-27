package com.softserve.teachua.model.test;

import com.softserve.teachua.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "questionTests"})
@Entity
@Table(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean archived;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private int difficulty;

    @Column(nullable = false)
    private int counter;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int grade;

    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;
}
