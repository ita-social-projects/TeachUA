package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

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

    @Column (nullable = false)
    private String title;

    @CreationTimestamp
    @Column
    private Timestamp date;

    @Column
    private String urlTitleLogo;

    @Column
    private String description;

}
