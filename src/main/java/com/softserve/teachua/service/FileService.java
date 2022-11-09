package com.softserve.teachua.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * This service contains methods to manage files
 */
public interface FileService {

    List<String> listFiles(String rootPath);

    String readFile(String name);

    ResponseEntity<Resource> downloadFile(String name);

    ResponseEntity<String> deleteFile(String name);

}
