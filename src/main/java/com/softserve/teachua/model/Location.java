package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.softserve.teachua.dto.marker.Convertible;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
@Entity
@Table(name = "locations")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Location implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @EqualsAndHashCode.Include
    private String address;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private String phone;

    @ManyToOne
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    @ToString.Exclude
    private District district;

    @ManyToOne
    @JoinColumn(name = "station_id", referencedColumnName = "id")
    @ToString.Exclude
    private Station station;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ToString.Exclude
    private City city;

    @ManyToOne
    @JsonBackReference(value = "locationReferens")
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    @ToString.Exclude
    private Club club;

    @ManyToOne
    @JsonBackReference(value = "locationReferensCenter")
    @JoinColumn(name = "center_id", referencedColumnName = "id")
    @ToString.Exclude
    private Center center;
}
