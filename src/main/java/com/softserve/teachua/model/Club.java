package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;
import com.softserve.teachua.model.GalleryPhoto;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@With
@Builder
@Entity
@Table(name = "clubs")
public class Club implements Convertible, Archivable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer ageFrom;

    @Column
    private Integer ageTo;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Column(columnDefinition = "TEXT", length = 1500)
    @Pattern(regexp = "^[А-Яа-яёЁЇїІіЄєҐґa-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]{40,1500}$",
            message = "This description isn`t correct")
    private String description;

    @Column
    private String urlWeb;

    @Column
    private String urlLogo;

    @Column
    private String urlBackground;

    @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    @ToString.Exclude
    private List<GalleryPhoto> urlGallery;

    @Column
    private String workTime;

    @Column
    private Double rating;

    @Column
    private Boolean isOnline;

    @OneToMany(mappedBy = "club")
    @JsonManagedReference
    @ToString.Exclude
    private Set<Location> locations;

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
    @JsonBackReference
    @ToString.Exclude
    private Center center;

    @Column
    private Boolean isApproved;

    @Column(length = 3000)
    private String contacts;

    @Column(name = "club_external_id")
    private Long clubExternalId;

    @Column(name = "center_external_id")
    private Long centerExternalId;
}