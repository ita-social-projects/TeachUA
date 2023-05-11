package com.softserve.teachua.service.impl;

import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.exception.MethodNotSupportedException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.StreamCloseException;
import com.softserve.teachua.model.AboutUsItem;
import com.softserve.teachua.model.BannerItem;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.ContactType;
import com.softserve.teachua.model.GalleryPhoto;
import com.softserve.teachua.model.News;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.AboutUsItemRepository;
import com.softserve.teachua.repository.BannerItemRepository;
import com.softserve.teachua.repository.CategoryRepository;
import com.softserve.teachua.repository.CenterRepository;
import com.softserve.teachua.repository.ChallengeRepository;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.ContactTypeRepository;
import com.softserve.teachua.repository.GalleryRepository;
import com.softserve.teachua.repository.NewsRepository;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.BackupService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class BackupServiceImpl implements BackupService {
    private static final String ALL_FILES = "all";
    private static final String IMAGES_DIRECTORY = "\\\\images";
    private static final String NOT_FOUND_FILE_EXCEPTION = "File %s not found";
    private static final String PUT_FILE_EXCEPTION = "Cant put file %s to zip archive";
    private static final String WRITE_FILE_EXCEPTION = "Cant write file %s to zip archive";
    private static final String CLOSE_FILE_UPLOAD_EXCEPTION = "Cant close file %s";
    private static final String CLOSE_STREAM_EXCEPTION = "Cant close stream";
    private static final String ZIP_STREAM_EXCEPTION = "Zip stream exception";
    private static final String FILE_READ_EXCEPTION = "Cant read file %s ";
    private static final String CANT_CREATE_DIRECTORY = "Cant create directory: %s";
    private static final String BACKUP_DIRECTORY = "src\\main\\resources\\Backup\\\\";
    private static final String SUCCESSFUL_ADDED_FILE = "File: %s Successfully added";
    private static final String CANT_TRANSFER_FILE = " Cant transfer file to directory %s";
    private static final String CANT_DELETE_DIRECTORY = "Cant delete directory %s";

    private final AboutUsItemRepository aboutUsItemRepository;
    private final BannerItemRepository bannerItemRepository;
    private final CategoryRepository categoryRepository;
    private final CenterRepository centerRepository;
    private final ClubRepository clubRepository;
    private final ContactTypeRepository contactTypeRepository;
    private final GalleryRepository galleryRepository;
    private final NewsRepository newsRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;

    public BackupServiceImpl(AboutUsItemRepository aboutUsItemRepository, BannerItemRepository bannerItemRepository,
                             CategoryRepository categoryRepository, CenterRepository centerRepository,
                             ClubRepository clubRepository, ContactTypeRepository contactTypeRepository,
                             GalleryRepository galleryRepository, NewsRepository newsRepository,
                             TaskRepository taskRepository, UserRepository userRepository,
                             ChallengeRepository challengeRepository) {
        this.aboutUsItemRepository = aboutUsItemRepository;
        this.bannerItemRepository = bannerItemRepository;
        this.categoryRepository = categoryRepository;
        this.centerRepository = centerRepository;
        this.clubRepository = clubRepository;
        this.contactTypeRepository = contactTypeRepository;
        this.galleryRepository = galleryRepository;
        this.newsRepository = newsRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
    }

    @Override
    public List<String> getAllBackupFiles(String fileName) {
        List<List<String>> filePathForBackup = new LinkedList<>();
        filePathForBackup.add(
                aboutUsItemRepository.findAll().stream().map(AboutUsItem::getPicture).collect(Collectors.toList()));
        filePathForBackup
                .add(bannerItemRepository.findAll().stream().map(BannerItem::getPicture).collect(Collectors.toList()));
        filePathForBackup
                .add(categoryRepository.findAll().stream().map(Category::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup.add(
                centerRepository.findAll().stream().map(Center::getUrlBackgroundPicture).collect(Collectors.toList()));
        filePathForBackup.add(centerRepository.findAll().stream().map(Center::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup
                .add(clubRepository.findAll().stream().map(Club::getUrlBackground).collect(Collectors.toList()));
        filePathForBackup.add(clubRepository.findAll().stream().map(Club::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup.add(
                contactTypeRepository.findAll().stream().map(ContactType::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup
                .add(galleryRepository.findAll().stream().map(GalleryPhoto::getUrl).collect(Collectors.toList()));
        filePathForBackup
                .add(newsRepository.findAll().stream().map(News::getUrlTitleLogo).collect(Collectors.toList()));
        filePathForBackup.add(taskRepository.findAll().stream().map(Task::getPicture).collect(Collectors.toList()));
        filePathForBackup.add(userRepository.findAll().stream().map(User::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup
                .add(challengeRepository.findAll().stream().map(Challenge::getPicture).collect(Collectors.toList()));
        filePathForBackup.add(FileUtils.listFiles(new File(IMAGES_DIRECTORY), null, false).stream().map(File::getName)
                .collect(Collectors.toList()));

        log.debug("**/ getting file list for backup");
        if (!fileName.equals(ALL_FILES)) {
            return filePathForBackup.stream().flatMap(Collection::stream).distinct()
                    .filter(file -> file != null && file.contains(fileName)).collect(Collectors.toList());
        }
        return filePathForBackup.stream().flatMap(Collection::stream).distinct()
                .filter(file -> file != null && !file.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public void downloadBackup(HttpServletResponse response) {
        ZipOutputStream zipStream = null;
        try {
            zipStream = new ZipOutputStream(response.getOutputStream());
        } catch (IOException e) {
            throw new FileUploadException(ZIP_STREAM_EXCEPTION);
        }

        for (String file : getAllBackupFiles(ALL_FILES)) {
            if (!(new File(file).exists())) {
                throw new NotExistException(String.format(NOT_FOUND_FILE_EXCEPTION, file));
            }
            byte[] buffer = new byte[1024];
            try {
                buffer = Files.readAllBytes(Paths.get(file));
            } catch (IOException e) {
                throw new FileUploadException(FILE_READ_EXCEPTION);
            }

            ZipEntry zipEntry = new ZipEntry(file);
            zipEntry.setSize(buffer.length);

            try {
                zipStream.putNextEntry(zipEntry);
            } catch (IOException e) {
                throw new FileUploadException(String.format(PUT_FILE_EXCEPTION, file));
            }
            try {
                zipStream.write(buffer);
            } catch (IOException e) {
                throw new FileUploadException(String.format(WRITE_FILE_EXCEPTION, file));
            }
            try {
                zipStream.closeEntry();
            } catch (IOException e) {
                throw new FileUploadException(String.format(CLOSE_FILE_UPLOAD_EXCEPTION, file));
            }
        }
        try {
            zipStream.flush();
            zipStream.close();
        } catch (IOException e) {
            throw new StreamCloseException(CLOSE_STREAM_EXCEPTION);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= \"TeachUaBackup.zip\"");
    }

    @Override
    public List<String> uploadBackup(MultipartFile file) throws IOException {
        List<String> movedFileList = new LinkedList<>();

        if (!(new File(BACKUP_DIRECTORY).isDirectory())) {
            new File(BACKUP_DIRECTORY).mkdir();
        }

        try {
            file.transferTo(Paths.get(BACKUP_DIRECTORY + file.getOriginalFilename()));
        } catch (FileUploadException e) {
            throw new FileUploadException(String.format(CANT_TRANSFER_FILE, BACKUP_DIRECTORY));
        }

        ZipEntry zipEntry;
        byte[] buffer = new byte[1024];

        try (ZipInputStream zipInputStream = new ZipInputStream(
                Files.newInputStream(Paths.get(BACKUP_DIRECTORY + file.getOriginalFilename())))) {
            zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String zipDirectory = zipEntry.getName().split("TeachUA\\\\")[1];
                File newFile = new File(zipDirectory);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdir()) {
                        throw new IOException(String.format(CANT_CREATE_DIRECTORY, newFile));
                    }
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException(String.format(CANT_CREATE_DIRECTORY, parent));
                    }

                    int len;
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zipInputStream.getNextEntry();

                movedFileList.add(String.format(SUCCESSFUL_ADDED_FILE, zipDirectory));
            }
        } catch (IOException e) {
            throw new FileUploadException(FILE_READ_EXCEPTION);
        }

        if (!(new File(BACKUP_DIRECTORY).delete())) {
            throw new MethodNotSupportedException(String.format(CANT_DELETE_DIRECTORY, BACKUP_DIRECTORY));
        }
        return movedFileList;
    }
}
