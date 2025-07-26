package com.myanime.service.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadFile(String id, MultipartFile file) throws IOException;
    void deleteFile(String id) throws IOException;
}
