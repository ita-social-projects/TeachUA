package com.softserve.teachua.model;

import com.softserve.amqp.marker.Archivable;
import com.softserve.commons.util.marker.Convertible;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
