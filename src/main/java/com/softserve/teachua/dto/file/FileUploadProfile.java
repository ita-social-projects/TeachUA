package com.softserve.teachua.dto.file;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class FileUploadProfile {

    @NotEmpty
    private String folder;
    @NotEmpty
    private String fileName;
    @NotEmpty
    private String base64;

}
