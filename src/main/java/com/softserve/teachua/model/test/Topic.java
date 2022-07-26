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

    @Column(nullable = false, unique = true)
    private String title;
}


