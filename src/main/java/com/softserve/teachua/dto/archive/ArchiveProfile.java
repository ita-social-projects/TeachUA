package com.softserve.teachua.dto.archive;

import com.softserve.teachua.model.marker.Archivable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ArchiveProfile {

    private String serviceClassName;

    private Archivable data;

}
