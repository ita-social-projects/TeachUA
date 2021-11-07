package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Data
@Entity
@Table(name = "users")
public class User implements Convertible, Archivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String password;

    @Column
    @Pattern(regexp = "^[А-Яа-яёЁЇїІіЄєҐґa-zA-Z()]{1,25}$", message = "This first name isn`t correct")
    private String firstName;

    @Column
    @Pattern(regexp = "^[А-Яа-яёЁЇїІіЄєҐґa-zA-Z()]{1,25}$", message = "This last name isn`t correct")
    private String lastName;

    @Column
    private String phone;

    @Column
    private String urlLogo;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Column
    private boolean status;

    @Column
    private String verificationCode;
}