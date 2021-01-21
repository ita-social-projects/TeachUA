package com.softserve.teachua.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToOne
    @JoinColumn(name = "coordinate_id")
    private Coordinate coordinate;

    @OneToMany(mappedBy = "city")
    private List<Club> clubs;
}
