package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.commons.marker.Archivable;
import com.softserve.teachua.service.impl.CategoryServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

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
    public Class<CategoryServiceImpl> getServiceClass() {
        return CategoryServiceImpl.class;
    }
}
