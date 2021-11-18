package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.file.FileUpdateProfile;
import com.softserve.teachua.dto.file.FileUploadProfile;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.GalleryPhoto;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.GalleryRepository;
import com.softserve.teachua.service.FileUploadService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.jasper.runtime.ExceptionUtils;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    private static final String UPLOAD_PLUG = "/upload/test/test.png";
    private static final Long IMAGE_SIZE_MB = 5L;
    private static final Long IMAGE_SIZE_B = IMAGE_SIZE_MB * 1024 * 1024;
    private static final Long MIN_IMAGE_WIDTH = 200L;
    private static final Long MIN_IMAGE_HEIGHT = 200L;
    private final String FILE_UPLOAD_EXCEPTION = "Could not save image file: %s";
    private final String DIRECTORY_CREATE_EXCEPTION = "Could not create directory with name: %s";
    private final String IMAGE_SIZE_EXCEPTION = "Max image size should be %d bytes, your image size is %d bytes";
    private final String IMAGE_RESOLUTION_EXCEPTION = "Image %s should be more than %d, your image %s is %d";
    private final String UPLOAD_LOCATION = "/upload";
    private final String TEMP_FILE_STORAGE = "src/main/resources/tempImage/";
    private final String TARGET = "target";
    private final GalleryRepository galleryRepository;
    private final ClubRepository clubRepository;
    @Value("${application.upload.path}")
    private String uploadDirectory;


    public FileUploadServiceImpl(GalleryRepository galleryRepository, ClubRepository clubRepository) {
        this.galleryRepository = galleryRepository;
        this.clubRepository = clubRepository;
    }

    @Override
    public String getPhoto(String filePath) {
        GalleryPhoto galleryPhoto = galleryRepository.findByUrl(filePath);
        File file = new File(TARGET + filePath);
        try {
            byte[] baseImage = FileUtils.readFileToByteArray(file);
            return Base64.getEncoder().encodeToString(baseImage);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        log.debug("get file from path" + filePath);
        return "File not found";
    }

    @Override
    public String uploadImage(FileUploadProfile uploadProfile) {
        //filePath - '/upload/...../fileName.extension'

        String uploadDir = String.format("%s/%s", uploadDirectory, uploadProfile.getFolder());
        encodeBase64(uploadProfile);

        File file = new File(TEMP_FILE_STORAGE + uploadProfile.getFileName());
        String fileName = StringUtils.cleanPath(file.getName());
        saveFile(uploadDir, fileName, file, uploadProfile.getBase64());

        String actualPath = String.format("/%s/%s", uploadDir, fileName);
        Optional<Club> optionalClub = clubRepository.findById(uploadProfile.getId());
        Club club = optionalClub.get();
        GalleryPhoto galleryPhoto = new GalleryPhoto();
        actualPath = actualPath.substring(7);
        galleryPhoto.setUrl(actualPath);
        galleryPhoto.setClub(club);

        galleryRepository.save(galleryPhoto);
        log.debug("added to db " + galleryPhoto.getUrl());
        return actualPath.substring(actualPath.indexOf(UPLOAD_LOCATION));
    }

    @Override
    public void deleteImages(String urlLogo, String urlBackground, List<GalleryPhoto> urlGallery) {
        //filePath - '/upload/...../fileName.extension'

        String folderName = null;
        try {
            if (urlLogo != null && urlLogo.contains(UPLOAD_LOCATION)) {
                folderName = urlLogo.substring(0, ordinalIndexOf(urlLogo, "/", 4, false));
            } else if (urlBackground != null && urlBackground.contains(UPLOAD_LOCATION)) {
                folderName = urlBackground.substring(0, ordinalIndexOf(urlBackground, "/", 4, false));
            } else if (urlGallery != null && !urlGallery.isEmpty()) {
                String url = urlGallery.get(0).getUrl();
                folderName = url.substring(0, ordinalIndexOf(url, "/", 4, false));
            }
        } catch (IndexOutOfBoundsException ex) {
            log.error("Incorrect photo url");
            log.error(e.getMessage(),e);
        }

        if (folderName != null) {
            try {
                FileUtils.deleteDirectory(new File(TARGET + folderName));
            } catch (IOException ex) {
                log.error("Folder " + folderName + " can not be deleted");
                log.error(e.getMessage(),e);
            }
        }
    }

    @Override
    public Boolean deleteFile(String filePath) {
        delete(filePath);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        localDelete(TEMP_FILE_STORAGE + fileName);
        GalleryPhoto galleryPhoto = galleryRepository.findByUrl(filePath);
        galleryRepository.delete(galleryPhoto);
        log.debug("Deleted from db successful");
        return true;
    }

    @Override
    public Boolean updatePhoto(FileUpdateProfile fileUpdateProfile) {
        //filePath - '/upload/...../fileName.extension'

        String filePath = fileUpdateProfile.getFilePath();
        GalleryPhoto galleryPhoto = galleryRepository.findByUrl(filePath);

        delete(filePath);

        String oldFileName = filePath.substring(filePath.lastIndexOf("/")+1);
        localDelete(TEMP_FILE_STORAGE+oldFileName);

        int firstEnter = (filePath.indexOf("/"));
        String clearPath = filePath.substring(filePath.indexOf("/", firstEnter + 1) + 1, filePath.lastIndexOf("/"));
        String uploadDir = String.format("%s/%s", uploadDirectory, clearPath);

        File file = new File(TEMP_FILE_STORAGE + fileUpdateProfile.getFileName());
        encodeBase64(fileUpdateProfile);
        saveFile(uploadDir, fileUpdateProfile.getFileName(), file, fileUpdateProfile.getBase64());

        uploadDir = uploadDir + "/" + fileUpdateProfile.getFileName();
        uploadDir = uploadDir.substring(uploadDir.indexOf("/"));


        galleryPhoto.setUrl(uploadDir);
        galleryRepository.save(galleryPhoto);
        log.debug("Saved to db successful");
        return true;
    }

    private void saveFile(String uploadDir, String fileName, File file, String byte64) {
        Path uploadPath = Paths.get(uploadDir);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(),e);
        }

        if (file.length() > IMAGE_SIZE_B) {
            try {
                file.deleteOnExit();
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
            throw new IncorrectInputException(String.format(IMAGE_SIZE_EXCEPTION, IMAGE_SIZE_B, file.length() / (1024 * 1024)));
        }

        try {
            Base64.Decoder decoder = Base64.getMimeDecoder();
            byte[] decod = decoder.decode(byte64);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decod));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if (width < MIN_IMAGE_WIDTH) {
                throw new IncorrectInputException(
                        String.format(IMAGE_RESOLUTION_EXCEPTION, "width", MIN_IMAGE_WIDTH, "width", width));
            }
            if (height < MIN_IMAGE_HEIGHT) {
                throw new IncorrectInputException(
                        String.format(IMAGE_RESOLUTION_EXCEPTION, "height", MIN_IMAGE_HEIGHT, "height", height));
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new FileUploadException(String.format(DIRECTORY_CREATE_EXCEPTION, fileName));
        }

        try {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new FileUploadException(String.format(FILE_UPLOAD_EXCEPTION, fileName));
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    private void localDelete(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IncorrectInputException("File path can not be null or empty");
        }
        if (!filePath.contains(TEMP_FILE_STORAGE)) {
            throw new IncorrectInputException("Wrong uploaded file path");
        }
        File file = new File(filePath);
        if (!file.isDirectory()) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    private void delete(String filePath) {
        if (filePath.contains(UPLOAD_PLUG)) {
        }
        if (filePath == null || filePath.isEmpty()) {
            throw new IncorrectInputException("File path can not be null or empty");
        }
        if (!filePath.contains(UPLOAD_LOCATION)) {
            throw new IncorrectInputException("Wrong uploaded file path");
        }
        File file = new File("target" + filePath);
        if (!file.isDirectory()) {
            file.deleteOnExit();
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    private int ordinalIndexOf(final String str, final String searchStr, final int ordinal, final boolean lastIndex) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        int index = lastIndex ? str.length() : -1;
        do {
            if (lastIndex) {
                index = str.lastIndexOf(searchStr, index - 1);
            } else {
                index = str.indexOf(searchStr, index + 1);
            }
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    private void encodeBase64(FileUploadProfile uploadProfile) {
        encode(uploadProfile.getBase64(), uploadProfile.getFileName());
    }

    private void encodeBase64(FileUpdateProfile uploadProfile) {
        encode(uploadProfile.getBase64(), uploadProfile.getFileName());
    }

    private void encode(String base64, String fileName) {
        Path folderPath = Paths.get(TEMP_FILE_STORAGE);
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Base64.Decoder decoder = Base64.getMimeDecoder();
        byte[] decod = decoder.decode(base64);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("./" + TEMP_FILE_STORAGE + fileName);
            fileOutputStream.write(decod);
            fileOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }

        Path path = Paths.get(TEMP_FILE_STORAGE);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }
}
