package com.softserve.teachua.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "club_contacts")
public class ClubContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String phoneNumbers;

    @Column
    private String email;

    @OneToOne(mappedBy = "clubContact")
    private Club club;

}

