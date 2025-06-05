package com.regisx001.blog.services.impl;

import com.regisx001.blog.services.FileStorageService;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorageServiceImpl implements FileStorageService {


    @Override
    public String saveImage(MultipartFile file, String filename) {
        return "";
    }
}
