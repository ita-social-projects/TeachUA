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
    public List<String> getAllLogs() {
        return FileUtils.listFiles(new File(path), null, false)
                .stream()
                .map(File::getName)
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

    //Зробити перевірку на те що приходить
    @Override
    public Boolean deleteAllLogs(String date,boolean singleDate) {
//        try {
        System.out.println(date);
//        List<String> logList = FileUtils.listFiles(new File(path), null, false)
//                .stream()
//                .map(File::getName)
//                .collect(Collectors.toList());
        List<String> logList1 = FileUtils.listFiles(new File(path), null, false)
                .stream()
                .map(File::getName)
                .filter(file -> file.contains(date))
                .collect(Collectors.toList());

        System.err.println(FileUtils.getFile("teachualogs-2021-10-11.1000.log").exists());
//        System.out.println("It`s for delete: "+logList1);

//        FileUtils.listFiles(new File(path),null,false);
//            FileUtils.cleanDirectory(new File(path));
//            
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
        return true;
    }
}