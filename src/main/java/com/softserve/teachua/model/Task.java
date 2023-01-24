package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Entity
@With
@Table(name = "tasks")
public class Task implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 10000)
    private String headerText;

    @Column(length = 10000)
    private String description;

    @Column(nullable = false)
    private String picture;

    @ManyToOne
    @JoinColumn(name = "challenge_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Challenge challenge;

    @Column
    private LocalDate startDate;
}
