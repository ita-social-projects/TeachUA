package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.CategoryServiceImpl;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class CategoryArch implements Convertible, Archivable {
    private Integer sortBy;
    private String name;
    private String description;
    private String urlLogo;
    private String backgroundColor;
    private String tagBackgroundColor;
    private String tagTextColor;

    @Override
    public Class getServiceClass() {
        return CategoryServiceImpl.class;
    }
}