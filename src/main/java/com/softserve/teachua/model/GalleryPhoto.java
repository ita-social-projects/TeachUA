package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Entity
@Table(name = "galleries")
public class GalleryPhoto implements Archivable, Convertible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @JsonBackReference(value = "club")
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "id", nullable = false)
    private Club club;
}
