package com.softserve.teachua.dto.user_challenge.admin;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChallengeForAdminGet implements Convertible {
    Long challengeId;
    String challengeName;
    Boolean isActive;
}
