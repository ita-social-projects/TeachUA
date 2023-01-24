package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
@Entity
@Table(name = "banner_items")
public class BannerItem implements Convertible {
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
