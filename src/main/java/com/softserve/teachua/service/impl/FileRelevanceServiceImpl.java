package com.softserve.teachua.service.impl;

import com.softserve.teachua.repository.*;
import com.softserve.teachua.service.FileRelevanceService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileRelevanceServiceImpl implements FileRelevanceService {

    private final AboutUsItemRepository aboutUsItemRepository;

    private final BannerItemRepository bannerItemRepository;

    private final CertificateTemplateRepository certificateTemplateRepository;

    private final ChallengeRepository challengeRepository;

    private final ClubRepository clubRepository;

    private final ContactTypeRepository contactTypeRepository;

    private final NewsRepository newsRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Autowired
    public FileRelevanceServiceImpl(AboutUsItemRepository aboutUsItemRepository,
                                    BannerItemRepository bannerItemRepository,
                                    CertificateTemplateRepository certificateTemplateRepository,
                                    ChallengeRepository challengeRepository,
                                    ClubRepository clubRepository,
                                    ContactTypeRepository contactTypeRepository,
                                    NewsRepository newsRepository,
                                    TaskRepository taskRepository,
                                    UserRepository userRepository) {
        this.aboutUsItemRepository = aboutUsItemRepository;
        this.bannerItemRepository = bannerItemRepository;
        this.certificateTemplateRepository = certificateTemplateRepository;
        this.challengeRepository = challengeRepository;
        this.clubRepository = clubRepository;
        this.contactTypeRepository = contactTypeRepository;
        this.newsRepository = newsRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Set<String> getAllMentionedFiles() {
        Set<String> files = new HashSet<>();

        aboutUsItemRepository.findAll().forEach(aboutUsItem -> files.add(aboutUsItem.getPicture()));
        bannerItemRepository.findAll().forEach(bannerItem -> files.add(bannerItem.getPicture()));
        certificateTemplateRepository.findAll()
                .forEach(certificateTemplate -> {
                    files.add(certificateTemplate.getFilePath());
                    files.add(certificateTemplate.getPicturePath());
                });
        challengeRepository.findAll().forEach(challenge -> files.add(challenge.getPicture()));
        clubRepository.findAll().forEach(club -> files.add(club.getUrlBackground()));
        contactTypeRepository.findAll().forEach(contactType -> files.add(contactType.getUrlLogo()));
        newsRepository.findAll().forEach(news -> files.add(news.getUrlTitleLogo()));
        taskRepository.findAll().forEach(task -> files.add(task.getPicture()));
        userRepository.findAll().forEach(user -> files.add(user.getUrlLogo()));

        return files;
    }

    public Set<String> getAllOrphanedFiles() {
        Collection<File> files = FileUtils.listFiles(
                new File(ORPHANED_FILES_SEARCH_PATH), ORPHANED_FILES_EXTENSIONS, true);
        Set<String> mentionedFiles = getAllMentionedFiles();
        return files.stream()
                .map(file -> "/" + file.getPath())
                .filter(file -> !mentionedFiles.contains(file))
                .collect(Collectors.toSet());
    }

}
