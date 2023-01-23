package com.softserve.teachua.dto.test.result;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleFormsInformation {
    private String title;

    private String description;
}
