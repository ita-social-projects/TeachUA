package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Builder
@With
@Entity
@Table(name = "challenges")
public class Challenge implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 30000)
    private String description;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false)
    private Long sortNumber;

    @Column
    private String registrationLink;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "challenge")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Task> tasks;
}
