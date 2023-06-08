package com.softserve.user.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.commons.util.marker.Convertible;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

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
