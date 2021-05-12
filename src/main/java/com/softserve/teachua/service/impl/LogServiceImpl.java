package com.softserve.teachua.service.impl;

import com.softserve.teachua.service.LogService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {

    @Value(value = "${logging.file.path}")
    private String PATH;

    @Override
    public List<String> getAllLogs() {
        return FileUtils.listFiles(new File(PATH), null, false)
                .stream()
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getLogByName(String name) {
        List<String> result = new ArrayList<>();

        try {
            result = FileUtils.readLines(new File(PATH + "/" + name), StandardCharsets.UTF_8);
            for (String s : result) {
                s += "\\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Boolean deleteAllLogs() {
        try {
            FileUtils.cleanDirectory(new File(PATH));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}