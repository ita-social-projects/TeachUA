package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Dto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@Entity
@Table(name = "cities")
public class City implements Dto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NonNull
    private String name;
}
