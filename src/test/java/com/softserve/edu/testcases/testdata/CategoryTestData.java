package com.softserve.edu.testcases.testdata;

import com.softserve.edu.models.placeholder.CategoryDto;

public class CategoryTestData {

    public CategoryTestData() {
        // Default constructor
    }

    public CategoryDto randomCategory() {
        return CategoryDto.newBuilder()
                .withId(6)
                .withSortBy(25)
                .withName("Вокальна студія, музика, музичні інструменти")
                .withDescription("Музична школа, хор, ансамбль, гра на музичних інструментах, звукорежисерський гурток та ін.")
                .withUrlLogo("/static/images/categories/music.svg")
                .withBackgroundColor("#FF7A45")
                .withTagBackgroundColor("#FF7A45")
                .withTagTextColor("#fff")
                .build();
    }

}
