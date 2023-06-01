package com.softserve.teachua.constants.excel;

public enum CertificateExcelColumn implements ExcelColumn {
    SURNAME("прізвище", "Відсутня колонка з ім'ям та прізвищем"),
    DATE("дата", "Відсутня колонка з датою видачі сертифікату"),
    EMAIL("електронна", "Відсутня колонка з електронною адресою");

    private final String keyWord;
    private final String missingMessage;

    CertificateExcelColumn(String keyWord, String missingMessage) {
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
