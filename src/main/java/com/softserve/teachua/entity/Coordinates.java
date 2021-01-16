package com.softserve.teachua.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="club_coordinates")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Coordinates {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @OneToOne(mappedBy = "coordinates")
    private Club club;
}
