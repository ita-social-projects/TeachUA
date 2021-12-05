package com.softserve.teachua.dto.log;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class LogResponse {
    private List<String> deletedLogs;
    private List<String> notDeletedLogs;

}
