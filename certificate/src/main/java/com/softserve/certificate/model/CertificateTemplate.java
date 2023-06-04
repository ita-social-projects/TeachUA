package com.softserve.certificate.model;

import com.softserve.clients.marker.Convertible;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Entity
@Table(name = "certificate_templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
public class CertificateTemplate implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "certificate_type", referencedColumnName = "id")
    private CertificateType certificateType;

    @Column(nullable = false, name = "file_path")
    private String filePath;

    @Column(name = "course_description")
    private String courseDescription;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "picture_path")
    private String picturePath;

    @Column(name = "properties")
    private String properties;
}
