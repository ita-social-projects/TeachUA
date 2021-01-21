package com.softserve.teachua.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "clubs")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer ageFrom;

    @Column
    private Integer ageTo;

    @Column
    private String name;

    @Column
    private String urlWeb;

    @Column
    private String urlLogo;

    @Column
    private String workTime;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @OneToMany(mappedBy = "club")
    private List<Feedback> feedbacks;

    @OneToOne
    @JoinColumn(name = "coordinate_id")
    private Coordinate coordinate;

    @OneToOne
    @JoinColumn(name = "club_contact_id")
    private ClubContact clubContact;
}
