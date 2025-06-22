package com.regisx001.blog.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.regisx001.blog.config.FileStorageConfig;
import com.regisx001.blog.services.StorageService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final FileStorageConfig fileStorageConfig;
    private Path avatarsLocation;

    @PostConstruct
    void init() {
        this.avatarsLocation = Paths.get(fileStorageConfig.getUploadDir(), "avatars");
        try {
            Files.createDirectories(avatarsLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize avatars storage", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "unknown";
        }
        String filename = UUID.randomUUID() + "-" + StringUtils.cleanPath(originalFilename);
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }

            Path destinationPath = this.avatarsLocation.resolve(Paths.get(filename));
            if (!destinationPath.normalize().startsWith(avatarsLocation)) {
                throw new RuntimeException("Cannot store file outside avatars directory");
            }
            file.transferTo(destinationPath);
            return filename;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file", ex);
        }
    }

}
