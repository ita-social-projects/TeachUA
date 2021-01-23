package com.softserve.teachua.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @OneToOne
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    @ToString.Exclude
    private Club club;
}

