package com.softserve.teachua.documentreport;

public interface ReportGenerationService<T> {
    byte[] getPdfOutput(T t);
}