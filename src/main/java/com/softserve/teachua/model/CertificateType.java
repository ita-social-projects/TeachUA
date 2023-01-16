package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import javax.persistence.*;
import lombok.*;

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
