package com.myanime.application.rest.controllers;

import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.domain.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/upload")
@RequiredArgsConstructor
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    @PostMapping("")
    public String upload(@RequestParam String id, @RequestPart MultipartFile file) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new AppException(ErrorCode.MISSING_REQUEST_PARAM);
        }

        if (file == null) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }

        return cloudinaryService.uploadFile(id, file);
    }

    @DeleteMapping("")
    public void delete(@RequestParam String id) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new AppException(ErrorCode.MISSING_REQUEST_PARAM);
        }

        cloudinaryService.deleteFile(id);
    }
}
