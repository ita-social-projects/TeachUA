package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
