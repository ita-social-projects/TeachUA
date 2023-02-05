package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.teachua.dto.marker.Convertible;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
