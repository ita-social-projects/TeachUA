package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.commons.util.marker.Convertible;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@With
@Entity
@Table(name = "categories")
public class Category implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Integer sortby;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Column
    private String description;

    @Column
    private String urlLogo;

    @Column
    private String backgroundColor;

    @Column
    private String tagBackgroundColor;

    @Column
    private String tagTextColor;

    @ManyToMany(mappedBy = "categories")
    @JsonBackReference(value = "clubsInCategory")
    @ToString.Exclude
    private Set<Club> clubs = new HashSet<>();
}
