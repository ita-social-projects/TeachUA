package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
@Entity
@Table(name = "banner_items")
public class BannerItem implements Convertible, Archivable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String subtitle;

    @Column
    private String link;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false)
    private Integer sequenceNumber;
}
