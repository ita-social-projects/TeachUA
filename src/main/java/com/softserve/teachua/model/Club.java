package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.teachua.dto.marker.Dto;
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
public class Club implements Dto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer ageFrom;

    @Column
    private Integer ageTo;

    @Column
    @EqualsAndHashCode.Include
    private String name;

    @Column
    @EqualsAndHashCode.Include
    private String email;

    @Column
    private String address;

    @Column
    private String phones;

    @Column
    private String description;

    @Column
    private String urlWeb;

    @Column
    private String urlLogo;

    @Column
    private String workTime;

    @Column
    private String socialLinks;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "center_id", referencedColumnName = "id")
    @ToString.Exclude
    private Center center;
}
