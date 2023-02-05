package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDate;
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
import lombok.ToString;
import lombok.With;

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

    @Column(nullable = true, unique = true, name = "serial_number")
    private Long serialNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

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
    private CertificateTemplate template;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dates_id", referencedColumnName = "id")
    @ToString.Exclude
    private CertificateDates dates;

    @Column(name = "`values`")
    private String values;
}
