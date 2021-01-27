package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder
@Entity
@Table(name = "clubs")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @EqualsAndHashCode.Include
    private Integer ageFrom;

    @Column
    @EqualsAndHashCode.Include
    private Integer ageTo;

    @Column
    @EqualsAndHashCode.Include
    private String name;

    @Column
    @EqualsAndHashCode.Include
    private String urlWeb;

    @Column
    private String urlLogo;

    @Column
    private String workTime;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ToString.Exclude
    private City city;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "club_category",
            joinColumns = {@JoinColumn(name = "club_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    @ToString.Exclude
    private Set<Category> categories = new HashSet<>();

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "club_coordinates",
            joinColumns = {@JoinColumn(name = "club_id")},
            inverseJoinColumns = {@JoinColumn(name = "coordinates_id")})
    @ToString.Exclude
    private Set<Coordinates> coordinates = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "studio_id", referencedColumnName = "id")
    @ToString.Exclude
    private Studio studio;
}
