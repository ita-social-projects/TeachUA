package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
@Entity
@Table(name = "roles")
public class Role implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @JsonManagedReference(value = "userRole")
    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    private List<User> users;
}
