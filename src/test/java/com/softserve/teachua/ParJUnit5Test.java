package com.softserve.teachua;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

@DisplayName("Pass the method parameters provided by the @ValueSource annotation")
public class ParJUnit5Test {

    @DisplayName("Should pass a non-null message to our test method")
    @ParameterizedTest
    @ValueSource(strings = {"Hello", "World"})
    void shouldPassNonNullMessageAsMethodParameter(String message) {
        System.out.println("Message = " + message);
        Assertions.assertNotNull(message);
    }

    @DisplayName("Should calculate the correct sum")
    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}")
    @CsvSource({
            "1, 1, 2",
            "2, 3, 5"
    })
    void sum(int a, int b, int sum) {
        System.out.printf("\na=%d, b=%d, sum=%d", a, b, sum);
        Assertions.assertEquals(sum, a + b);
    }

    @DisplayName("Should calculate the correct sum")
    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}")
    @CsvFileSource(resources = "/test-data.csv")
    void sum2(int a, int b, int sum) {
        System.out.printf("\na=%d, b=%d, sum=%d", a, b, sum);
        Assertions.assertEquals(sum, a + b);
    }

    private static Stream<Arguments> sumProvider() {
        return Stream.of(
                Arguments.of(1, 1, 2),
                Arguments.of(2, 3, 5)
        );
    }

    @DisplayName("Should calculate the correct sum")
    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}")
    @MethodSource("sumProvider")
    void sum3(int a, int b, int sum) {
        System.out.printf("\na=%d, b=%d, sum=%d", a, b, sum);
        Assertions.assertEquals(sum, a + b);
    }
}
