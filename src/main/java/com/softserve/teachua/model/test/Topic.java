package com.softserve.teachua.model.test;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;
}


