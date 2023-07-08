package com.softserve.certificate.model;

import com.softserve.amqp.marker.Archivable;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "certificate_dates")
@With
@Builder
public class CertificateDates implements Convertible, Archivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String date;

    @Column
    private Integer hours;

    @Column
    private String duration;

    @Column(name = "course_number")
    private String courseNumber;

    @Column(name = "study_form")
    private String studyForm;
}
