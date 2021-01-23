package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "roles")
public class Role {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    private List<User> users;
}
