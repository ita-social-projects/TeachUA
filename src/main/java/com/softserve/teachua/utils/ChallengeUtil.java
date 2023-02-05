package com.softserve.teachua.utils;

import com.softserve.teachua.exception.IncorrectInputException;
import java.util.List;

public class ChallengeUtil {
    public static void validateSortNumber(Long sortNumber, List<Long> unavailableSortNumbers) {
        if (unavailableSortNumbers.contains(sortNumber) || sortNumber <= 0) {
            throw new IncorrectInputException("Sort number must be unique and greater than 0");
        }
    }
}
