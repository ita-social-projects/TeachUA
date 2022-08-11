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

    @Column(unique = true, name = "serial_number")
    private Long serialNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "send_status")
    private Boolean sendStatus;

    @Column(name="update_status")
    private LocalDate updateStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    @JsonBackReference(value = "certificateTemplate")
    @ToString.Exclude
    private CertificateTemplate template;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dates_id", referencedColumnName = "id")
    @ToString.Exclude
    private CertificateDates dates;
}
