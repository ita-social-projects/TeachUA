package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Data
@Entity
@Table(name = "challenges_durations")
public class ChallengeDuration implements Convertible {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column()
    private boolean userExist;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "challenge_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Challenge challenge;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "duration_entity_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private DurationEntity durationEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChallengeDuration)) {
            return false;
        }
        ChallengeDuration that = (ChallengeDuration) o;
        return userExist == that.userExist && Objects.equals(id, that.id)
            && Objects.equals(challenge, that.challenge)
            && Objects.equals(durationEntity, that.durationEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userExist, challenge, durationEntity);
    }

    @Override
    public String toString() {
        return "ChallengeDuration{"
            + "id=" + id
            + ", userExist=" + userExist
            + ", durationEntity=" + durationEntity
            + '}';
    }
}
