package com.softserve.teachua.model.test;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "question_types")
public class QuestionType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;
}
