package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.file.FilePathRequest;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.GalleryPhoto;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.GalleryRepository;
import com.softserve.teachua.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    private final String FILE_UPLOAD_EXCEPTION = "Could not save image file: %s";
    private final String DIRECTORY_CREATE_EXCEPTION = "Could not create directory with name: %s";
    private final String IMAGE_SIZE_EXCEPTION = "Max image size should be %d bytes, your image size is %d bytes";
    private final String IMAGE_RESOLUTION_EXCEPTION = "Image %s should be more than %d, your image %s is %d";
    private final String UPLOAD_LOCATION = "/upload";
    private static final String UPLOAD_PLUG = "/upload/test/test.png";
    private static final Long IMAGE_SIZE_MB = 5L;
    private static final Long IMAGE_SIZE_B = IMAGE_SIZE_MB * 1024 * 1024;
    private static final Long MIN_IMAGE_WIDTH = 200L;
    private static final Long MIN_IMAGE_HEIGHT = 200L;

    private final GalleryRepository galleryRepository;
    private final ClubRepository clubRepository;

    public FileUploadServiceImpl(GalleryRepository galleryRepository, ClubRepository clubRepository) {
        this.galleryRepository = galleryRepository;
        this.clubRepository = clubRepository;
    }

    @Override
    public File getPhoto(FilePathRequest filePath) {
        GalleryPhoto galleryPhoto  = galleryRepository.findByUrl(filePath.getFilePath());
        //TODO : set right correctly return image
        return new File("\\target"+filePath.getFilePath());
      //  return new File("\\target\\upload\\imag\\logo400x400.png");
    }

    @Override
    public String uploadImage(String uploadDir, String fileName, MultipartFile multipartFile,Long id) {
        saveFile(uploadDir, fileName, multipartFile);

        String actualPath = String.format("/%s/%s", uploadDir, fileName);
        Optional<Club> optionalClub = clubRepository.findById(id);
        actualPath = actualPath.substring(7,actualPath.length());
        Club club = optionalClub.get();
        GalleryPhoto galleryPhoto = new GalleryPhoto();
        galleryPhoto.setUrl(actualPath);
        galleryPhoto.setClub(club);

        galleryRepository.save(galleryPhoto);
        return actualPath.substring(actualPath.indexOf(UPLOAD_LOCATION));
    }

    @Override
    public void deleteImages(String urlLogo, String urlBackground, List<GalleryPhoto> urlGallery) {
        // assuming that path to folder will look like target/upload/clubs/uuidv4

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
        }

        if (folderName != null) {
            try {
                FileUtils.deleteDirectory(new File("target" + folderName));
            } catch (IOException ex) {
                log.error("Folder " + folderName + " can not be deleted");
            }
        }
    }
    //filePath - '/upload/...../fileName.extension'

    @Override
    public String deleteFile(String filePath) {
        delete(filePath);
        GalleryPhoto galleryPhoto = galleryRepository.findByUrl(filePath);
        galleryRepository.delete(galleryPhoto);
        return filePath+" successfully deleted.";
    }

    @Override
    public String updatePhoto(MultipartFile file,String filePath,String uploadDirectory) {
        // filePath --/upload/.../file.
       delete(filePath);

        int firstEnter = (filePath.indexOf("/"));
        String clearPath = filePath.substring(filePath.indexOf("/",firstEnter+1)+1,filePath.lastIndexOf("/")) ;
        String uploadDir = String.format("%s/%s", uploadDirectory, clearPath);

        saveFile(uploadDir, file.getOriginalFilename(), file);

        GalleryPhoto galleryPhoto = galleryRepository.findByUrl(filePath);

        uploadDir = uploadDir+"/"+ file.getOriginalFilename();
        uploadDir = uploadDir.substring(uploadDir.indexOf("/"));

        galleryPhoto.setUrl(uploadDir);
        galleryRepository.save(galleryPhoto);

        return "File changed successful";
    }

    private void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir);


        System.out.println(uploadDir);

        if(multipartFile.getSize() > IMAGE_SIZE_B){
            throw new IncorrectInputException(String.format(IMAGE_SIZE_EXCEPTION, IMAGE_SIZE_B, multipartFile.getSize()));
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if(width < MIN_IMAGE_WIDTH){
                throw new IncorrectInputException(
                        String.format(IMAGE_RESOLUTION_EXCEPTION, "width", MIN_IMAGE_WIDTH, "width", width));
            }
            if(height < MIN_IMAGE_HEIGHT){
                throw new IncorrectInputException(
                        String.format(IMAGE_RESOLUTION_EXCEPTION, "height", MIN_IMAGE_HEIGHT, "height", height));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new FileUploadException(String.format(DIRECTORY_CREATE_EXCEPTION, fileName));
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new FileUploadException(String.format(FILE_UPLOAD_EXCEPTION, fileName));
        }
    }

    private void delete(String filePath){
        if (filePath.contains(UPLOAD_PLUG)) {

        }
        if (filePath == null || filePath.isEmpty()) {
            throw new IncorrectInputException("File path can not be null or empty");
        }
        if (!filePath.contains(UPLOAD_LOCATION)) {
            throw new IncorrectInputException("Wrong uploaded file path");
        }
        File file = new File("/target" + filePath);
        if(!file.isDirectory()){
            file.deleteOnExit();
            file.exists();
        }
        try {
            FileUtils.forceDelete(new File("target" + filePath));
        } catch (IOException e) {
            e.printStackTrace();
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

}
