package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

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
