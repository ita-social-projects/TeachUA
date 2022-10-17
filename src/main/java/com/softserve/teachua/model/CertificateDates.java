package com.softserve.teachua.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "certificate_dates")
@With
@Builder
public class CertificateDates {

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

    @Column(name = "course_description")
    private String courseDescription;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "picture_path")
    private String picturePath;
}
