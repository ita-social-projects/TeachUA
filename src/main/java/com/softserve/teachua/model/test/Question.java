package com.softserve.teachua.model.test;

import com.softserve.teachua.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "answers", "questionTests"})
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private QuestionType questionType;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "question")
    private Set<QuestionTest> questionTests = new HashSet<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "question")
    private Set<Answer> answers = new HashSet<>();

    public Set<QuestionTest> getQuestionTests() {
        return Collections.unmodifiableSet(questionTests);
    }

    public void addQuestionTest(QuestionTest questionTest) {
        questionTests.add(questionTest);
    }
}
