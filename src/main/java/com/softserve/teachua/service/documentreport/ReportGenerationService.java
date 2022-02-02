package com.softserve.teachua.service.documentreport;

public interface ReportGenerationService<T> {
    byte[] getPdfOutput(T t);
}