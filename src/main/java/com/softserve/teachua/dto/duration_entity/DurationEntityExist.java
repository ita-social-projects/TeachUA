package com.softserve.teachua.dto.duration_entity;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DurationEntityExist implements Convertible {
    boolean durationEntityExist;
}
