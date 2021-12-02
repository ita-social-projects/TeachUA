package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.*;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Club implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column
    private Integer ageFrom;

    @Column
    private Integer ageTo;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String urlWeb;

    @Column
    private String urlLogo;

    @Column
    private String urlBackground;

    @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<GalleryPhoto> urlGallery;

    @Column
    private String workTime;

    @Column
    @ColumnDefault(value = "0")
    private Double rating;

    @Column(name = "feedback_count")
    @ColumnDefault(value = "0")
    private Long feedbackCount;

    @Column
    private Boolean isOnline;

    @OneToMany(mappedBy = "club")
    @ToString.Exclude
    private Set<Location> locations;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "club_category",
            joinColumns = {@JoinColumn(name = "club_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    @ToString.Exclude
    private Set<Category> categories = new HashSet<>();

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "center_id", referencedColumnName = "id")
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