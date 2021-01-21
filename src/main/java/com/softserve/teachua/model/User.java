package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    private String email;

    @NonNull
    @Column
    private String password;

    @NonNull
    @Column
    private String name;

    @NonNull
    @ToString.Exclude
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    List<Club> clubs;
}
