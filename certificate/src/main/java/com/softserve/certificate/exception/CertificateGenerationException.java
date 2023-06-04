package com.softserve.certificate.exception;

public class CertificateGenerationException extends RuntimeException {
    private static final String CERTIFICATE_GENERATION_EXCEPTION = "Помилка генерації pdf сертифікату";

    public CertificateGenerationException() {
        super(CERTIFICATE_GENERATION_EXCEPTION);
    }
}
