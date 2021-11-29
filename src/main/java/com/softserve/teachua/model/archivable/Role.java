package com.softserve.teachua.model.archivable;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
@With
@Entity
@Table(name = "roles")
public class Role implements Convertible {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    private List<User> users;
}
