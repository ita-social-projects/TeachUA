package com.softserve.teachua.model.test;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "question_test")
public class QuestionTest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;
}
