package com.softserve.teachua.dto.category;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResponse implements Convertible {

    private Long id;

    private String name;

    private String urlLogo;

    private String backgroundColor;

   //TODO
    // private Set<Club> clubs;
}
