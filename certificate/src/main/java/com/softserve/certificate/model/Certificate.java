package com.softserve.certificate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
@EqualsAndHashCode
public class Certificate implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "serial_number")
    private Long serialNumber;

    @Column(name = "user_id")
    @ToString.Exclude
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String sendToEmail;

    @Column(name = "messenger_name")
    private String messengerUserName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "messenger_id", referencedColumnName = "id")
    @ToString.Exclude
    private Messenger messenger;

    @Column(name = "send_status")
    private Boolean sendStatus;

    @Column(name = "update_status")
    private LocalDate updateStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    @JsonBackReference(value = "certificateTemplate")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CertificateTemplate template;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dates_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CertificateDates dates;

    @Column(name = "`values`")
    private String values;
}
