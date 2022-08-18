package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@With
@Builder
@Entity
@Table(name = "about_us_items")
public class AboutUsItem implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 6000)
    private String text;

    private String picture;

    private String video;

    @Column(nullable = false)
    private Long type;

    @Column(nullable = false)
    private Long number;
}
