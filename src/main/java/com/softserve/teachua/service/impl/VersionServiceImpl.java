package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.version.VersionDto;
import com.softserve.teachua.dto.version.VersionEnum;
import com.softserve.teachua.service.PropertiesService;
import com.softserve.teachua.service.VersionService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class VersionServiceImpl implements VersionService {
    private static final String VERSION_FILE_NAME = "version.properties";
    private static final String BACKAND_PATH = "./.git";
    private static final String BRANCH_HEAD = "HEAD";
    private PropertiesService propertiesService;

    @Autowired
    public VersionServiceImpl(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    public VersionDto getVersion() {
        Map<String, String> versionProperties = propertiesService.readProperties(VERSION_FILE_NAME);
        VersionDto versionDto = VersionDto.builder()
                .backendCommitNumber(versionProperties.get(VersionEnum.BACKEND_COMMIT_NUMBER.getFieldName()))
                .backendCommitDate(versionProperties.get(VersionEnum.BACKEND_COMMIT_DATE.getFieldName()))
                .buildDate(versionProperties.get(VersionEnum.BUILD_DATE.getFieldName()).replace("\\",""))
                .build();
        log.debug("VersionService = " + versionDto);
        return versionDto;
    }

    public void setVersion() {
        RevCommit commit = getCommit();
        propertiesService.writeProperties(VERSION_FILE_NAME, commit.getName(), commit.getCommitTime());
    }

    public RevCommit getCommit() {
        RevCommit commit = null;
        try {
            Repository existingRepo = new FileRepositoryBuilder()
                    .setGitDir(new File(BACKAND_PATH))
                    .build();
            ObjectId head = existingRepo.resolve(BRANCH_HEAD);
            RevWalk walk = new RevWalk(existingRepo);
            commit = walk.parseCommit(head);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return commit;
    }
}
