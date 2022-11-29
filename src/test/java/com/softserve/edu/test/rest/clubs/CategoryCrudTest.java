package com.softserve.edu.test.rest.clubs;

import com.softserve.edu.models.placeholder.CategoryDto;
import com.softserve.edu.services.api.Services;
import com.softserve.edu.testcases.testdata.ApiTestData;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class CategoryCrudTest {

    @Test
    public void testGettingCategoryById() {
        CategoryDto categoryDto = ApiTestData.categoryTestData().randomCategory();
        int categoryId = categoryDto.getId();
        CategoryDto category = Services.placeHolderApi().category().getCategoryById(categoryId);

        // Verification
        Assertions.assertThat(category)
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .isEqualTo(categoryDto);
    }

}
