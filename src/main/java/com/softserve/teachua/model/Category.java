package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column
    @EqualsAndHashCode.Include
    private String name;

    @Column
    private String urlLogo;

    @JsonBackReference
    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
    private Set<Club> clubs = new HashSet<>();
}
