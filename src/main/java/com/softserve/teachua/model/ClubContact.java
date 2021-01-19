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

    @Column(name = "phone_numbers")
    private String phoneNumbers;

    @Column(name = "email")
    private String email;

    @ManyToOne
    private Club club;

    @ManyToOne
    private User contactPerson;
}

