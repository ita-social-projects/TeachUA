package com.softserve.teachua.dto.challenge;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessagesChallenge implements Convertible {
    private Long id;
    private String name;
}
