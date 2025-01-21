package com.myanime.controller;

import com.myanime.exception.AppException;
import com.myanime.exception.ErrorCode;
import com.myanime.service.cloudinary.CloudinaryService;
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

        return cloudinaryService.uploadFile(file);
    }
}
