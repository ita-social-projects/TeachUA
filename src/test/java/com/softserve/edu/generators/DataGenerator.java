package com.softserve.edu.generators;

import java.util.Random;

public class DataGenerator {

    // ASCII codes to limit data generation
    private static final int LEFT_UPPER_LIMIT = 65;                         // numeral 'A'
    private static final int RIGHT_UPPER_LIMIT = 90;                        // numeral 'Z'
    private static final int LEFT_LOWER_LIMIT = 97;                         // letter 'a'
    private static final int RIGHT_LOWER_LIMIT = 122;                       // letter 'z'
    private static final int LEFT_NUMBER_LIMIT = 48;                        // numeral '0'
    private static final int RIGHT_NUMBER_LIMIT = 57;                       // numeral '9'
    private static final Random random = new Random();

    // Generate random alphabetic string with a certain length
    public String generateRandomAlphabeticString(int length) {
        return random.ints(LEFT_UPPER_LIMIT, RIGHT_LOWER_LIMIT + 1)         // Range where to get characters
                // ASCII code range that should be omitted while generating value
                .filter(i -> (i <= 90 || i >= 97))
                .limit(length)                                              // String size
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // Generate random alphabetic uppercase string with a certain length
    public String generateRandomAlphabeticUpperString(int length) {
        return random.ints(LEFT_UPPER_LIMIT, RIGHT_UPPER_LIMIT + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // Generate random alphabetic lowercase string with a certain length
    public String generateRandomAlphabeticLowerString(int length) {
        return random.ints(LEFT_LOWER_LIMIT, RIGHT_LOWER_LIMIT + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // Generate random alphanumeric string with a certain length
    public String generateRandomAlphanumericString(int length) {
        return random.ints(LEFT_NUMBER_LIMIT, RIGHT_LOWER_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // Generate random alphanumeric lowercase string with a certain length
    public String generateRandomAlphanumericLowerString(int length) {
        return random.ints(LEFT_NUMBER_LIMIT, RIGHT_LOWER_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // Generate random numeric string with a certain length
    public String generateRandomNumericString(int length) {
        return random.ints(LEFT_NUMBER_LIMIT, RIGHT_NUMBER_LIMIT + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // Generate random integer value from a certain range
    public int generateRandomIntegerNumber(int minValue, int maxValue) {
        return random.ints(minValue, maxValue)
                .findFirst()
                .getAsInt();
    }

    // Generate random double value from a certain range
    public double generateRandomDoubleNumber(double minValue, double maxValue) {
        return minValue + new Random().nextFloat() * (maxValue - minValue);
    }

    // Generate random boolean value
    public boolean generateRandomBooleanValue() {
        return random.nextBoolean();
    }

    // Generate random email
    public String generateRandomEmail() {
        return generateRandomAlphanumericLowerString(10) + "@" + generateRandomAlphabeticLowerString(4) + ".com";
    }

    // Generate random name with a certain length
    public String generateRandomName(int length) {
        return generateRandomAlphabeticUpperString(1) + generateRandomAlphabeticLowerString(length-1);
    }

}
