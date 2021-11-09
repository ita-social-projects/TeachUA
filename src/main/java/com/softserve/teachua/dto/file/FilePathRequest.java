package com.softserve.teachua.dto.file;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FilePathRequest {

    @NotEmpty
    private String filePath;

}
