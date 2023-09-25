package com.example.aaa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/audio")
public class AudioFileController {
    private final FileStorageService fileService;

    public AudioFileController(FileStorageService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.storeFile(file);
    }

    @GetMapping("/fileInfo")
    public ResponseEntity<FileDto> getFile(@RequestParam String fileName) throws IOException {
        return ResponseEntity.ok(fileService.getFileByName(fileName));
    }
}

