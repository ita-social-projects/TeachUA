package com.softserve.teachua.service.impl;


import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.StreamCloseException;
import com.softserve.teachua.model.*;
import com.softserve.teachua.repository.*;
import com.softserve.teachua.service.BackupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

@Service
@Slf4j
public class BackupServiceImpl implements BackupService {

    private final static String ALL_FILES = "all";
    private final static String IMAGES_DIRECTORY = "images";
    private final static String NOT_FOUND_FILE_EXCEPTION = "File %s not found";
    private final static String PUT_FILE_EXCEPTION = "Can`t put file %s to zip archive";
    private final static String WRITE_FILE_EXCEPTION = "Can`t write file %s to zip archive";
    private final static String CLOSE_FILE_UPLOAD_EXCEPTION = "Cant close file %s";
    private final static String CLOSE_STREAM_EXCEPTION = "Cant close stream";
    private final static String ZIP_STREAM_EXCEPTION = "Zip stream exception";
    private final static String FILE_READ_EXCEPTION = "Cant read file %s ";

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
        filePathForBackup.add(aboutUsItemRepository.findAll().stream().map(AboutUsItem::getPicture).collect(Collectors.toList()));
        filePathForBackup.add(bannerItemRepository.findAll().stream().map(BannerItem::getPicture).collect(Collectors.toList()));
        filePathForBackup.add(categoryRepository.findAll().stream().map(Category::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup.add(centerRepository.findAll().stream().map(Center::getUrlBackgroundPicture).collect(Collectors.toList()));
        filePathForBackup.add(centerRepository.findAll().stream().map(Center::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup.add(clubRepository.findAll().stream().map(Club::getUrlBackground).collect(Collectors.toList()));
        filePathForBackup.add(clubRepository.findAll().stream().map(Club::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup.add(contactTypeRepository.findAll().stream().map(ContactType::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup.add(galleryRepository.findAll().stream().map(GalleryPhoto::getUrl).collect(Collectors.toList()));
        filePathForBackup.add(newsRepository.findAll().stream().map(News::getUrlTitleLogo).collect(Collectors.toList()));
        filePathForBackup.add(taskRepository.findAll().stream().map(Task::getPicture).collect(Collectors.toList()));
        filePathForBackup.add(userRepository.findAll().stream().map(User::getUrlLogo).collect(Collectors.toList()));
        filePathForBackup.add(challengeRepository.findAll().stream().map(Challenge::getPicture).collect(Collectors.toList()));
        filePathForBackup.add(FileUtils.listFiles(new File(IMAGES_DIRECTORY), null, false).stream().map(File::getName).collect(Collectors.toList()));

        log.debug("**/ getting file list for backup");
        if (!fileName.equals(ALL_FILES)) {
            return filePathForBackup
                    .stream()
                    .flatMap(Collection::stream)
                    .distinct()
                    .filter(file -> file != null && file.contains(fileName))
                    .collect(Collectors.toList());
        }
        return filePathForBackup.stream()
                .flatMap(Collection::stream)
                .distinct()
                .filter(file -> file != null && !file.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public void unloadBackup(HttpServletResponse response) {
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
            byte[] buffer = new byte[0];
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
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "\"attachment; filename= backup");

    }

    @Override
    public void uploadBackup(HttpServletRequest request) throws IOException {
        System.err.println(1);
        ZipInputStream zipInputStream = null;
        ZipEntry zipEntry = null;
        byte[] buffer = new byte[1024];
        System.err.println(2);
        File destDir = new File("src/main/resources/unzipTest");
        try {
            zipInputStream = new ZipInputStream(request.getInputStream());
        } catch (IOException e) {
            throw new FileUploadException(ZIP_STREAM_EXCEPTION);
        }
        System.err.println(3);
        try {
            zipEntry = zipInputStream.getNextEntry();
        } catch (IOException e) {
            throw new FileUploadException(FILE_READ_EXCEPTION);
        }
        System.out.println(zipInputStream);
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            System.err.println("Zip file name:" + zipEntry.getName());    //TODO for test, delete before push

            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdir()) {
                    throw new IOException("Cant create directory" + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Cant create directory" + parent);
                }

                System.err.println(4);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zipInputStream.getNextEntry();
        }
        System.err.println(5);

        try {
            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (IOException e) {
            throw new StreamCloseException(CLOSE_STREAM_EXCEPTION);
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
