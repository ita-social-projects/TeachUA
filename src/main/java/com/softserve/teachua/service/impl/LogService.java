package com.softserve.teachua.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogService {
    private static final String PATH = "target/logs";

    public Map<String, List<String>> getAllLogs() {
        Map<String, List<String>> result = new HashMap<>();

        List<File> files = (List<File>) FileUtils.listFiles(new File(PATH), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

        for (int i = 0; i < files.size(); i++) {
            try {
                result.put(files.get(i).getName(), FileUtils.readLines(files.get(i), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public Boolean deleteAllLogs() {

        return true;
    }
}