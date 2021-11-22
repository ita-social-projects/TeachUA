package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.file.FileUpdateProfile;
import com.softserve.teachua.dto.file.FileUploadProfile;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.GalleryPhoto;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.GalleryRepository;
import com.softserve.teachua.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
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
    private final int TARGET_LENGTH = 7;
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
            File file = new File(TARGET + filePath);
        try {
            byte[] baseImage = FileUtils.readFileToByteArray(file);
            Optional<String> s = Optional.ofNullable(Base64.getEncoder().encodeToString(baseImage));
            return s.isPresent() ? s.get() : s.orElseThrow(IOException::new);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                log.debug("get file from path" + filePath);
                throw new FileUploadException("Some problems while file reading ");
            }
    }

    @Override
    public String uploadImage(FileUploadProfile uploadProfile) {
        //filePath - '/upload/...../fileName.extension'
        String uploadDir = String.format("%s/%s", uploadDirectory, uploadProfile.getFolder());
        File file = new File(TEMP_FILE_STORAGE + uploadProfile.getFileName());
        String fileName = StringUtils.cleanPath(file.getName());
        String actualPath = String.format("/%s/%s", uploadDir, fileName);
        if (file.exists()){
            throw new FileUploadException("File exist");
        }
        encodeBase64(uploadProfile);
        saveFile(uploadDir, fileName, file, uploadProfile.getBase64());
        log.debug("File "+fileName+" wrote successful");
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
            log.error(ex.getMessage(),ex);
        }

        if (folderName != null) {
            try {
                FileUtils.deleteDirectory(new File(TARGET + folderName));
            } catch (IOException ex) {
                log.error("Folder " + folderName + " can not be deleted");
                log.error(ex.getMessage(),ex);
            }
        }
    }

    @Override
    public Boolean deleteFile(String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        if(!delete(filePath) || !localDelete(TEMP_FILE_STORAGE + fileName)){
            return false;
        }
        log.debug("Deleted  successful");
        return true;
    }

    @Override
    public Boolean updatePhoto(FileUpdateProfile fileUpdateProfile) {
        //filePath - '/upload/...../fileName.extension'

        String filePath = fileUpdateProfile.getFilePath();
        String oldFileName = filePath.substring(filePath.lastIndexOf("/")+1);

        if(!delete(filePath) || !localDelete(TEMP_FILE_STORAGE+oldFileName)){
            return false;
        }

        String clearPath = filePath.substring(filePath.indexOf("/", (filePath.indexOf("/")) + 1) + 1, filePath.lastIndexOf("/"));
        String uploadDir = String.format("%s/%s", uploadDirectory, clearPath);

        File file = new File(TEMP_FILE_STORAGE + fileUpdateProfile.getFileName());
        encodeBase64(fileUpdateProfile);
        saveFile(uploadDir, fileUpdateProfile.getFileName(), file, fileUpdateProfile.getBase64());

        log.debug("File "+fileUpdateProfile.getFileName()+" deleted successful");
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

    private boolean localDelete(String filePath) {
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
                return false;
            }
        }
        return true;
    }

    private boolean delete(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IncorrectInputException("File path can not be null or empty");
        }
        if (!filePath.contains(UPLOAD_LOCATION)) {
            throw new IncorrectInputException("Wrong uploaded file path");
        }
        File file = new File(TARGET + filePath);

        if (!file.isDirectory()) {
            file.deleteOnExit();
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                return false;
            }
        }
        return true;
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
                log.error(e.getMessage(),e);
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
