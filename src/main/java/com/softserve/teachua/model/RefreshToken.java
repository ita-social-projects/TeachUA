package com.softserve.teachua.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@With
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "token", nullable = false, unique = true)
    String token;

    public void revoke() {
        user.setRefreshToken(null);
        setUser(null);
    }
}
