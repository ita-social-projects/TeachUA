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

    @ToString.Exclude
    @OneToMany(mappedBy = "test_id")
    private Set<QuestionTest> questionTests = new HashSet<>();

    public Set<QuestionTest> getQuestionTests() {
        return Collections.unmodifiableSet(questionTests);
    }

    public void addQuestionTest(QuestionTest questionTest) {
        questionTests.add(questionTest);
    }
}
