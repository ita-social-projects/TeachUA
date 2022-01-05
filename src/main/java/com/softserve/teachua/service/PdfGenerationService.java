package com.softserve.teachua.service;

public interface PdfGenerationService<T> {
    byte[] getPdfOutput(T t);
}