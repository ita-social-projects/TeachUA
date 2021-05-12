package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@With
@Builder
@Entity
@Table(name = "centers")
public class Center implements Convertible, Archivable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String phones;

    private String contacts;

    @Column(name = "url_background_picture")
    private String urlBackgroundPicture;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column
    private String urlWeb;

    @Column
    private String urlLogo;

    @Column
    private String socialLinks;

    @OneToMany(mappedBy = "center")
    @JsonManagedReference
    @ToString.Exclude
    private Set<Location> locations;

    @OneToMany(mappedBy = "center")
    @JsonManagedReference
    @ToString.Exclude
    private Set<Club> clubs;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

}
