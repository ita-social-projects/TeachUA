package com.softserve.teachua.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userName;

    @Column
    private Float rate;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

}
