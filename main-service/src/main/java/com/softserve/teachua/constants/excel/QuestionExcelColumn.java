package com.softserve.teachua.constants.excel;

public enum QuestionExcelColumn implements ExcelColumn {
    TITLE("назва", "Відсутня колонка з назвою запитання"),
    DESCRIPTION("опис", "Відсутня колонка з описом запитання"),
    TYPE("тип", "Відсутня колонка з типом запитання"),
    CATEGORY("категорія", "Відсутня колонка з категорією запитання"),
    ANSWERS("варіанти", "Відсутня колонка з варіантами відповідь до запитання"),
    CORRECT("правильна", "Відсутня колонка з правильними відповідями до запитання"),
    VALUE("оцінка", "Відсутня колонка з оцінками для запитання");

    private final String keyWord;
    private final String missingMessage;

    QuestionExcelColumn(String keyWord, String missingMessage) {
        this.keyWord = keyWord;
        this.missingMessage = missingMessage;
    }

    @Override
    public String getKeyWord() {
        return keyWord;
    }

    @Override
    public String getMissingMessage() {
        return missingMessage;
    }
}
