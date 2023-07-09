package com.softserve.teachua.model;

import com.softserve.amqp.marker.Archivable;
import com.softserve.commons.util.marker.Convertible;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Entity
@Builder
@Table(name = "news")
public class News implements Convertible, Archivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private LocalDate date;

    @Column
    private String urlTitleLogo;

    @Column(length = 10000)
    private String description;

    @Column
    private Boolean isActive;

    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private Long userId;
}
