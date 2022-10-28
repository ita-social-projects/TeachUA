package com.softserve.teachua.model.test;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "question_histories")
public class QuestionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "result_id", referencedColumnName = "id")
    private Result result;

    @ManyToOne(optional = false)
    @JoinColumn(name = "answer_id", referencedColumnName = "id")
    private Answer answer;
}
