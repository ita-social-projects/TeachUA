package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.softserve.teachua.dto.marker.Convertible;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "centers")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Center implements Convertible {
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

    @OneToMany(mappedBy = "center", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Location> locations;

    @OneToMany(mappedBy = "center", fetch = FetchType.EAGER)
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

    @Column(name = "feedback_count")
    @ColumnDefault(value = "0")
    private Long feedbackCount;

    @Column(name = "club_count")
    @ColumnDefault(value = "0")
    private Long clubCount;
}
