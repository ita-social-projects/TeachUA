package com.softserve.teachua.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "certificate_templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
public class CertificateTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "certificate_type")
    private Integer certificateType;

    @Column(nullable = false, name = "file_path")
    private String filePath;

    @Column(name = "course_description")
    private String courseDescription;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "picture_path")
    private String picturePath;

}