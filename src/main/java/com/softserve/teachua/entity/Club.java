package com.softserve.teachua.entity;

import com.softserve.teachua.entity.enums.ChildrenAge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "club")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "club_name")
    private String clubName;
    @Column(name = "web_site_url")
    private String webSiteUrl;
    @Column(name = "link_in_social_networks")
    private String linkInSocialNetworks;
    @Column(name = "general_description_of_activities")
    private String generalDescriptionOfActivities;
    @Column(name = "general_description_of_club")
    private String generalDescriptionOfClub;
    @Column(name = "logo_url")
    private String logoUrl;
    @Column(name = "children_age")
    @Enumerated(value = EnumType.STRING)
    @ElementCollection(targetClass = ChildrenAge.class)
    private List<ChildrenAge> childrenAge;
    @ManyToOne
    @JoinColumn(name = "children_center_id")
    private ChildrenCenter childrenCenter;
    @OneToMany(mappedBy = "club")
    private List<ContactPhoneNumber> contactPhoneNumbers;
    @ManyToMany
    @JoinTable(name = "club_activities",
    joinColumns = {@JoinColumn(name = "club_id")},
    inverseJoinColumns = {@JoinColumn(name = "activities_id")})
    private Set<Activities> activities;


}
