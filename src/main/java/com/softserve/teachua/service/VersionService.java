package com.softserve.teachua.service;

import com.softserve.teachua.dto.version.VersionDto;
import org.eclipse.jgit.revwalk.RevCommit;

public interface VersionService {
    VersionDto getVersion();

    void setVersion();

    RevCommit getCommit();
}
