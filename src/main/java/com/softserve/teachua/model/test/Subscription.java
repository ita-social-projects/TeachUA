package com.softserve.teachua.model.test;

import com.softserve.teachua.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@ToString(of = {"group", "user"})
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @Column(nullable = false)
    private LocalDate expirationDate;
}
