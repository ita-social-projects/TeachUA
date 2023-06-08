package com.softserve.teachua.model.test;

import com.softserve.commons.util.marker.Convertible;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"title", "description"})
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

    @Column(name = "creator_id")
    private Integer creatorId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private QuestionType questionType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private QuestionCategory questionCategory;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "question",
        orphanRemoval = true,
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
