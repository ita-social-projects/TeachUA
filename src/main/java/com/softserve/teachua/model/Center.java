package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@With
@Builder
@Entity
@Table(name = "centers")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Center implements Convertible, Archivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Column(length = 3000)
    private String contacts;

    @Column(name = "url_background_picture")
    private String urlBackgroundPicture;

    @Column(columnDefinition = "TEXT", length = 1500)
    private String description;

    @Column
    private String urlWeb;

    @Column
    private String urlLogo;

    @OneToMany(mappedBy = "center")
    @ToString.Exclude
    private Set<Location> locations;

    @OneToMany(mappedBy = "center")
    @ToString.Exclude
    private Set<Club> clubs;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Column(name = "center_external_id")
    private Long centerExternalId;

    @Column(name = "rating")
    @ColumnDefault(value = "0")
    private Double rating;

    @Column(name = "club_count")
    @ColumnDefault(value = "0")
    private Long clubCount;
}
