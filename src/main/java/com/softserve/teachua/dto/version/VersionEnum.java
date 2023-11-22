package com.softserve.teachua.dto.version;

public enum VersionEnum {
    BACKEND_COMMIT_NUMBER("backendCommitNumber"),
    BACKEND_COMMIT_DATE("backendCommitDate"),
    BUILD_DATE("buildDate");

    private String fieldName;

    private VersionEnum(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return "VersionEnum{"
                + "fieldName='" + fieldName + '\''
                + '}';
    }
}
