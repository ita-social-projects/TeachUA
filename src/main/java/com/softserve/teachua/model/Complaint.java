package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@With
@Builder
@Entity
@Table(name = "complaints")
public class Complaint implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Club club;

    @Column(nullable = false)
    private String text;
}
