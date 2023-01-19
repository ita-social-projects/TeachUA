package com.softserve.teachua.dto.test.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GoogleFormsQuizResults {
    private String respondentEmail;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private String lastSubmittedTime;

    private Double totalScore;
}
