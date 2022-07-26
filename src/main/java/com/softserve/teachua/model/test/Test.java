package com.softserve.teachua.model.test;

import com.softserve.teachua.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private boolean archived;

    @Column
    private boolean active;

    @Column
    private int difficulty;

    @Column
    private int counter;

    @Column
    private int duration;

    @Column
    private int grade;

    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "test_id")
    private Set<QuestionTest> questionTest = new HashSet<>();
}
