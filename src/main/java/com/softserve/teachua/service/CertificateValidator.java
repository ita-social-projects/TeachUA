package com.softserve.teachua.service;

import java.util.List;

public interface CertificateValidator {
    boolean validateNaturalNumber(String value, List<String[]> resultList, String errorDescription,
                                  String messageDescription);

    boolean validateEmail(String value, List<String[]> resultList, String messageDescription);

    boolean validateUserName(String value, List<String[]> resultList, String messageDescription);

    boolean validateDate(String value, List<String[]> resultList, String messageDescription);
}
