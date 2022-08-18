package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Entity
@Builder
@Table(name = "question")
public class Question implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    @Type(type = "text")
    private String text;
}
