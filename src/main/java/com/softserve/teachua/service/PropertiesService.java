package com.softserve.teachua.service;

import java.util.Map;

public interface PropertiesService {
    Map<String, String> readProperties(String fileName);

    void writeProperties(String fileName, String commitName, int commitDateTime);
}
