package com.softserve.teachua.entity;

import com.softserve.teachua.entity.enams.ChildrenAge;
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
@Table
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "web_site_url")
    private String webSiteUrl;
    @Column(name = "link_in_social_networks")
    private String linkInSocialNetworks;
    @Column(name = "general_description_of_activities")
    private String generalDescriptionOfActivities;
    @Column(name = "general_description_of_club")
    private String generalDescriptionOfClub;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "children_age")
    private ChildrenAge childrenAge;

    @ManyToOne
    private ChildrenCenter childrenCenter;
    @OneToMany
    private List<ContactPhoneNumber> contactPhoneNumbers;
    @ManyToMany
    private Set<Activities> activities;

}
