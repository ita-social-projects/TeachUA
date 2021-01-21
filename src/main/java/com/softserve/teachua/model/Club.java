package com.softserve.teachua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @JoinColumn(name ="city_id", referencedColumnName = "id")
    @JsonBackReference
    @ToString.Exclude
    private City city;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    @ToString.Exclude
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "studio_id")
    @JsonBackReference
    @ToString.Exclude
    private Studio studio;

    @OneToMany(mappedBy = "club")
    @JsonBackReference
    @ToString.Exclude
    private List<Feedback> feedbacks;

    @OneToOne
    @JoinColumn(name = "coordinate_id")
    @JsonBackReference
    @ToString.Exclude
    private Coordinate coordinate;

    @OneToOne
    @JoinColumn(name = "club_contact_id")
    @JsonBackReference
    @ToString.Exclude
    private ClubContact clubContact;
}
