package com.softserve.teachua.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
