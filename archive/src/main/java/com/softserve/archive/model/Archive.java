package com.softserve.archive.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document
public class Archive {
    @Id
    private String id;

    @Field("class_name")
    private String className;

    private Map<String, String> data;
}
