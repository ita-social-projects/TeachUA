package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Dto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "centers")
public class Center implements Dto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String address;

    @Column
    private String phones;

    @Column
    private String description;

    @Column
    private String urlWeb;

    @Column
    private String urlLogo;

    @Column
    private String socialLinks;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

}
