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
    private String path;

    @Override
    public List<String> getAllLogs(String filter) {
        return FileUtils.listFiles(new File(path), null, false)
                .stream()
                .map(File::getName)
                .filter(name -> name.contains(filter))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getLogByName(String name) {
        List<String> result = new ArrayList<>();

        try {
            result = FileUtils.readLines(new File(path + "/" + name), StandardCharsets.UTF_8);
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
            FileUtils.cleanDirectory(new File(path));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
