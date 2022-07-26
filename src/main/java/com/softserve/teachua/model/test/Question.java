package com.softserve.teachua.model.test;

import com.softserve.teachua.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private QuestionType questionType;

    @OneToMany(mappedBy = "question_id")  // ask
    Set<QuestionTest> questionTest;

    @OneToMany(mappedBy = "question_id")
    Set<Answer> answers;
}
