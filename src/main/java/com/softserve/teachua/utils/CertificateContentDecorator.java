package com.softserve.teachua.utils;

import org.springframework.stereotype.Component;

@Component
public class CertificateContentDecorator {
    public String formHours(Integer hours){
        return "Тривалість навчання - " + hours + " годин.";
    }
}
