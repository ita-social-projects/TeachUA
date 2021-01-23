package com.softserve.teachua.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "coordinates")
public class Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @OneToOne
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    @ToString.Exclude
    private Club club;
}
