package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@With
@Entity
@Table(name = "categories")
public class Category implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column (nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Column
    private String urlLogo;

    @Column
    private String backgroundColor;

    @JsonBackReference
    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
    private Set<Club> clubs = new HashSet<>();
}
