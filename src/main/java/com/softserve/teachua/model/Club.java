package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.softserve.teachua.dto.marker.Convertible;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import org.hibernate.annotations.ColumnDefault;

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

    @OneToMany(mappedBy = "club", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<GalleryPhoto> urlGallery;

    //@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "club_work_time",
            joinColumns = {@JoinColumn(name = "club_id")},
            inverseJoinColumns = {@JoinColumn(name = "work_time_id")})
    @ToString.Exclude
    private Set<WorkTime> workTimes;

    @Column
    @ColumnDefault(value = "0")
    private Double rating;

    @Column(name = "feedback_count")
    @ColumnDefault(value = "0")
    private Long feedbackCount;

    @Column
    private Boolean isOnline;

    @OneToMany(mappedBy = "club", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Location> locations;

    @OneToMany(mappedBy = "club", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Feedback> feedbacks;

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
