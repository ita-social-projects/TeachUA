package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.log.LogResponse;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LogServiceImpl implements LogService {
    @Value(value = "${logging.file.path}")
    private String path;
    private  static final String DELETING_EXCEPTION = "File %s didnt delete";
    private DtoConverter dtoConverter;

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

    @Override
    public LogResponse deleteLogsByFilter(String filter) {
        AtomicBoolean correctFilter= new AtomicBoolean(false);
        List <String> deletedLogs = new LinkedList<>();
        List <String> notDeletedLogs = new LinkedList<>();

        if (filter.equals("deleteAll")){
            FileUtils.listFiles(new File(path),null,false).stream().forEach(
                    file -> {
                        if (!file.getName().contains("catalina")){
                            correctFilter.set(true);
                            try {
                                FileUtils.forceDelete(new File(file.getAbsolutePath().replace(".\\","").replace(" \\","")));
                                deletedLogs.add("deleted: "+file.getName());
                            } catch (IOException e ) {
                                 notDeletedLogs.add("NOT deleted: "+file.getName());
                            }
                        }
                    }
            );
        }else {
            FileUtils.listFiles(new File(path),null,false).stream().forEach(
                    file -> {
                        if(!file.getName().contains("catalina") && file.getName().contains(filter)){
                            correctFilter.set(true);
                            try {
                                FileUtils.forceDelete(new File(file.getAbsolutePath().replace(".\\","").replace(" \\","")));
                                deletedLogs.add(" deleted: "+file.getName());
                            } catch (IOException e) {
                                notDeletedLogs.add("NOT deleted: "+file.getName());
                            }
                        }
                    }
            );
        }
        if (!correctFilter.get()){
            throw  new NotExistException("Not found file by this filter");
        }
        LogResponse logResponse = new LogResponse().withDeletedLogs(deletedLogs).withNotDeletedLogs(notDeletedLogs);
        log.debug("**/log delete");
        return logResponse;
    }

    @Override
    public List<String> getAbsolutePathForLogs() {

        List<String> pathList = new LinkedList<>();
        FileUtils.listFiles(new File(path),null,false)
                .stream()
                .forEach(file -> {
                    if (!file.getName().contains("catalina")) {
                        pathList.add(file.getAbsolutePath());
                    }
                });

        return pathList;
    }
}