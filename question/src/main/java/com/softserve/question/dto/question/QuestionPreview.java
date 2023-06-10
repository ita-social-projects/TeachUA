package com.softserve.question.dto.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionPreview implements Convertible {
    private Long id;
    private String title;
    private String description;
    private String category;
}
