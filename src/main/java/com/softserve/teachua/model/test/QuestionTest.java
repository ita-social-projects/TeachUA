package com.softserve.teachua.model.test;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
@Entity
@Table(name = "questions_tests")
public class QuestionTest {   // ask
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    Test test;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    Question question;
}
