package com.softserve.certificate.dto.googleapis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleFormsResponse {
    private String title;
    private Integer totalPoints;
    private List<QuizResult> quizResults;
}
