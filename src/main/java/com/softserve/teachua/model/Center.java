package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;

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

    @Column(columnDefinition="TEXT", length = 1500)
    @Pattern(regexp = "^[А-Яа-яёЁЇїІіЄєҐґa-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]{40,1500}$",
            message = "This description isn`t correct")
    private String description;

    @Column
    private String urlWeb;

    @Column
    private String urlLogo;

    @OneToMany(mappedBy = "center")
    @JsonManagedReference(value = "location-center")
//    @JsonManagedReference
    @ToString.Exclude
    private Set<Location> locations;

    @OneToMany(mappedBy = "center")
//    @JsonManagedReference
    @JsonManagedReference(value = "club-center")
    @ToString.Exclude
    private Set<Club> clubs;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Column(name = "center_external_id")
    private Long centerExternalId;
}
