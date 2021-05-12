package com.softserve.teachua.service;

import java.util.List;

public interface LogService {

    List<String> getAllLogs();

    List<String> getLogByName(String name);

    Boolean deleteAllLogs();
}