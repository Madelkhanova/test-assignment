package com.example.aaa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);


    private final Path fileStorageLocation;
    private  AudioFileRepository audioFileRepository;
    private  ResourceLoader resourceLoader;

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    public void setAudioFileRepository(AudioFileRepository audioFileRepository) {
        this.audioFileRepository = audioFileRepository;
    }

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new IOException("Invalid file name: " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileExtension = fileName.substring(fileName.lastIndexOf("."));

            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
            String formattedDate = dateFormat.format(new Date());

            String newFileName = formattedDate + fileExtension;

            AudioFile audioFile = new AudioFile();
            audioFile.setFileName(newFileName);
            audioFile.setFileDate(new Date());

            audioFile.setFilePath(targetLocation.toString());

            audioFileRepository.save(audioFile);

            logger.info("Файл {} успешно сохранен.", newFileName);

            return fileName;
        } catch (IOException ex) {
            logger.error("Ошибка при сохранении файла: {}", ex.getMessage());

            throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public FileDto getFileByName(String fileName) {
        Optional<AudioFile> audioFile = audioFileRepository.findByFileName(fileName);

        FileDto fileDto = new FileDto();

        if (audioFile.isPresent()) {
            logger.info("Файл найден: {}", audioFile.get().getFileName());

            fileDto.setFileDate(audioFile.get().getFileDate());
            fileDto.setFileName(audioFile.get().getFileName());
            fileDto.setFilePath(audioFile.get().getFilePath());

            Resource resource =  resourceLoader
                    .getResource(audioFile.get().getFilePath());

            if (resource.exists()) {
                fileDto.setResource(resource);
            }
        }
        return fileDto;
    }
}

