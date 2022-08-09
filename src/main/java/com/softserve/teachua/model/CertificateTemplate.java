package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.util.List;

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

//    @JsonManagedReference(value = "certificateType")
//    @OneToMany(mappedBy = "type")
//    @ToString.Exclude
//    private List<Certificate> certificates;
}