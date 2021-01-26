package com.softserve.teachua.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String urlLogo;

    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
    private Set<Club> clubs = new HashSet<>();
}
