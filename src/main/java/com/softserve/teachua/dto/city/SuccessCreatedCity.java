package com.softserve.teachua.dto.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedCity {
    private Long id;
    private String name;
}
