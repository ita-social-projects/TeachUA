package com.softserve.teachua.dto.test.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleFormsInformation {
    private String title;

    private String description;
}
