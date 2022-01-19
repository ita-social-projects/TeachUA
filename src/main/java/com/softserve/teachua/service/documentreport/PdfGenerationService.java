package com.softserve.teachua.service.documentreport;

public interface PdfGenerationService<T> {
    byte[] getPdfOutput(T t);
}