package com.softserve.certificate.model;

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

@Entity
@Table(name = "certificate_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
public class CertificateType implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, name = "code_number")
    private Integer codeNumber;
    @Column(nullable = false)
    private String name;
}
