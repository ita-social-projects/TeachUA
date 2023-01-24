package com.softserve.teachua.model.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
