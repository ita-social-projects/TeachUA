package com.softserve.teachua.service.impl;

import com.softserve.teachua.repository.AboutUsItemRepository;
import com.softserve.teachua.repository.BannerItemRepository;
import com.softserve.teachua.repository.ChallengeRepository;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.ContactTypeRepository;
import com.softserve.teachua.repository.GalleryRepository;
import com.softserve.teachua.repository.NewsRepository;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.FileRelevanceService;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileRelevanceServiceImpl implements FileRelevanceService {
    private static final String ORPHANED_FILES_SEARCH_PATH = "upload";

    private static final String[] ORPHANED_FILES_EXTENSIONS = {"png", "jpg"};

    private final AboutUsItemRepository aboutUsItemRepository;

    private final BannerItemRepository bannerItemRepository;
    //todo
    //private final CertificateTemplateRepository certificateTemplateRepository;

    private final ChallengeRepository challengeRepository;

    private final ClubRepository clubRepository;

    private final ContactTypeRepository contactTypeRepository;

    private final GalleryRepository galleryRepository;

    private final NewsRepository newsRepository;

    private final TaskRepository taskRepository;


    @Autowired
    public FileRelevanceServiceImpl(AboutUsItemRepository aboutUsItemRepository,
                                    BannerItemRepository bannerItemRepository,
                                    ChallengeRepository challengeRepository,
                                    ClubRepository clubRepository,
                                    ContactTypeRepository contactTypeRepository,
                                    GalleryRepository galleryRepository,
                                    NewsRepository newsRepository,
                                    TaskRepository taskRepository) {
        this.aboutUsItemRepository = aboutUsItemRepository;
        this.bannerItemRepository = bannerItemRepository;
        this.challengeRepository = challengeRepository;
        this.clubRepository = clubRepository;
        this.contactTypeRepository = contactTypeRepository;
        this.galleryRepository = galleryRepository;
        this.newsRepository = newsRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Set<String> getAllMentionedFiles() {
        Set<String> files = new HashSet<>();

        aboutUsItemRepository.findAll().forEach(aboutUsItem -> files.add(aboutUsItem.getPicture()));
        bannerItemRepository.findAll().forEach(bannerItem -> files.add(bannerItem.getPicture()));
        //todo
        //certificateTemplateRepository.findAll()
        //        .forEach(certificateTemplate -> {
        //            files.add(certificateTemplate.getFilePath());
        //            files.add(certificateTemplate.getPicturePath());
        //        });
        challengeRepository.findAll().forEach(challenge -> files.add(challenge.getPicture()));
        clubRepository.findAll().forEach(club -> files.add(club.getUrlBackground()));
        contactTypeRepository.findAll().forEach(contactType -> files.add(contactType.getUrlLogo()));
        galleryRepository.findAll().forEach(galleryPhoto -> files.add(galleryPhoto.getUrl()));
        newsRepository.findAll().forEach(news -> files.add(news.getUrlTitleLogo()));
        taskRepository.findAll().forEach(task -> files.add(task.getPicture()));
        //userRepository.findAll().forEach(user -> files.add(user.getUrlLogo()));

        return files;
    }

    public Set<String> getAllOrphanedFiles() {
        File directory = new File(ORPHANED_FILES_SEARCH_PATH);
        if (!directory.isDirectory()) {
            return new HashSet<>();
        }
        Collection<File> files = FileUtils.listFiles(
                directory, ORPHANED_FILES_EXTENSIONS, true);
        Set<String> mentionedFiles = getAllMentionedFiles();
        return files.stream()
                .map(file -> "/" + file.getPath())
                .filter(file -> !mentionedFiles.contains(file))
                .collect(Collectors.toSet());
    }
}
