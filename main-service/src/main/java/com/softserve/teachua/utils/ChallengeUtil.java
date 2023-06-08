package com.softserve.teachua.utils;

import com.softserve.commons.exception.IncorrectInputException;
import java.util.List;

public class ChallengeUtil {
    private ChallengeUtil() {
    }

    public static void validateSortNumber(Long sortNumber, List<Long> unavailableSortNumbers) {
        if (unavailableSortNumbers.contains(sortNumber) || sortNumber <= 0) {
            throw new IncorrectInputException("Sort number must be unique and greater than 0");
        }
    }
}
