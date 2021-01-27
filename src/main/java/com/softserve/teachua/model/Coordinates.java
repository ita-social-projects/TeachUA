package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @JsonBackReference
    @ManyToMany(mappedBy = "coordinates")
    @ToString.Exclude
    private Set<Club> clubs = new HashSet<>();
}
