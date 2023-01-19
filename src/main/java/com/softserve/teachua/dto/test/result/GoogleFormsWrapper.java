package com.softserve.teachua.dto.test.result;

import java.util.List;
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
public class GoogleFormsWrapper {
    private GoogleFormsInformation information;

    private List<GoogleFormsQuizResults> quizResults;
}
