package com.softserve.teachua.model.test;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.User;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"title", "description"})
@Entity(name = "testQuestion")
@Table(name = "questions")
public class Question implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private QuestionCategory questionCategory;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "question",
               cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Answer> answers = new HashSet<>();

    public Set<Answer> getAnswers() {
        return Collections.unmodifiableSet(answers);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }
}
