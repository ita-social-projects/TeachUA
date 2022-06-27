package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Entity
@Builder
@Table(name = "news")
public class News implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private LocalDate date;

    @Column
    private String urlTitleLogo;

    @Column//(length = 10000)
    private String description;

    @Column
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;
}
