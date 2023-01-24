package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
