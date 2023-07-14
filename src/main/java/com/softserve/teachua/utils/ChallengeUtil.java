package com.softserve.teachua.utils;

import com.softserve.teachua.exception.IncorrectInputException;
import java.util.List;
import java.util.stream.LongStream;

public class ChallengeUtil {
    private ChallengeUtil() {
    }

    public static void validateSortNumber(Long sortNumber, List<Long> unavailableSortNumbers) {
        if (unavailableSortNumbers.contains(sortNumber) || sortNumber <= 0) {
            throw new IncorrectInputException("Sort number must be unique and greater than 0");
        }
    }
    public static long generateUniqueSortNumber(List<Long> unavailableSortNumbers, long start) {
        int max = Integer.MAX_VALUE;
        return LongStream.range(start, max)
                .filter(n -> !unavailableSortNumbers.contains(n))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to generate a unique sort number."));
    }
}
