package com.softserve.teachua.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CertificateContentDecorator {
    public String formHours(Integer hours){
        return "Тривалість навчання - " + hours + " годин.";
    }

    public String fromDates(LocalDate startDate, LocalDate endDate) {
        return "з " + startDate.getDayOfMonth() + " травня по " + endDate.getDayOfMonth() + " червня " + endDate.getYear() + " року";
    }
}
