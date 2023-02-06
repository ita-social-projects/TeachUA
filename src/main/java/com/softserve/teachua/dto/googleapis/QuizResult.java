package com.softserve.teachua.dto.googleapis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResult {
    private String userEmail;

    private String fullName;

    private Integer totalScore;
}
