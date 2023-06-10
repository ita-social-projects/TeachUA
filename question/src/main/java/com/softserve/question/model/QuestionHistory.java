package com.softserve.question.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
