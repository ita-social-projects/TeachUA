package com.softserve.certificate.service;

import com.softserve.certificate.constants.MessageType;
import java.util.List;
import org.springframework.data.util.Pair;

public interface CertificateValidator {
    boolean validateNaturalNumber(String value, List<Pair<String, MessageType>> resultList, String errorDescription,
                                  String messageDescription);

    boolean validateEmail(String value, List<Pair<String, MessageType>> resultList, String messageDescription);

    boolean validateUserName(String value, List<Pair<String, MessageType>> resultList, String messageDescription);

    boolean validateDate(String value, List<Pair<String, MessageType>> resultList, String messageDescription);
}
