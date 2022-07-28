package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "certificate_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class CertificateType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String file_path;

    @JsonManagedReference(value = "certificateType")
    @OneToMany(mappedBy = "type")
    @ToString.Exclude
    private List<Certificate> certificates;
}