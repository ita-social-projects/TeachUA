package com.softserve.teachua.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "clubs")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "age_from")
    private Integer ageFrom;

    @Column(name = "age_to")
    private Integer ageTo;

    @Column
    private String name;

    @Column(name = "url_web")
    private String urlWeb;

    @Column(name = "url_logo")
    private String urlLogo;

    //TODO change to @OneToMany center
    @Column(name = "center_id")
    private Long centerId;

    //TODO change to @OneToMany category
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "work_time")
    private String workTime;

    @ManyToMany
    @JoinTable(
            name = "club_activities",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id"))
    Set<Activity> activities;
}
