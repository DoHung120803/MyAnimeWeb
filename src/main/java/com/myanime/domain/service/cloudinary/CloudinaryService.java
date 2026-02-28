package com.myanime.domain.service.cloudinary;

import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.dtos.MediaInfoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadFile(String id, MultipartFile file) throws IOException, BadRequestException;
    MediaInfoDTO uploadFile(MultipartFile file, String prefix) throws IOException, BadRequestException;
    void deleteFile(String id) throws IOException;
}
