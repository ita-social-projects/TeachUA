package com.softserve.teachua.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "ratings")
public class Rating {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "feedback")
    private String feedback;

    @ManyToOne
    private Club club;

    @ManyToOne
    private User user;
}
