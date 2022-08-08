package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
public class Certificate implements Convertible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userEmail;

    @Column
    private Boolean sendStatus;

    @Column
    private LocalDate updateStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    @JsonBackReference(value = "certificateTemplate")
    @ToString.Exclude
    private CertificateTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dates_id", referencedColumnName = "id")
    @ToString.Exclude
    private CertificateDates dates;
}
