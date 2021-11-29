package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
@Entity
@Table(name = "districts")
public class District implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private City city;
}
