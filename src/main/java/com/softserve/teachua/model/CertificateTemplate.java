package com.softserve.teachua.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "certificate_templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class CertificateTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String filePath;
}