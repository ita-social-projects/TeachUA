package com.softserve.club.util.documentreport;

public interface ReportGenerationService<T> {
    byte[] getPdfOutput(T t);
}
