package com.myanime.application.rest.controllers;

import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.domain.dtos.MediaInfoDTO;
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
    public ApiResponse<MediaInfoDTO> upload(@RequestPart(required = false) String prefix, @RequestPart MultipartFile file) throws IOException, BadRequestException {
        if (file == null) {
            throw new BadRequestException("File không được để trống");
        }

        return ApiResponse.<MediaInfoDTO>builder()
                .data(cloudinaryService.uploadFile(file, prefix))
                .build();
    }

    @DeleteMapping("")
    public void delete(@RequestParam String id) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new AppException(ErrorCode.MISSING_REQUEST_PARAM);
        }

        cloudinaryService.deleteFile(id);
    }
}
