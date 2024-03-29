package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
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
public class CertificateDates implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
