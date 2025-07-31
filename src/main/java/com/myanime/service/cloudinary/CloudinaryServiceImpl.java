package com.myanime.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.myanime.exception.AppException;
import com.myanime.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final long MAX_FILE_SIZE = 2097152;

    private final Cloudinary cloudinary;


    @Override
    public String uploadFile(String id, MultipartFile file) throws IOException {
        checkValidFile(file);
        Map<?, ?> upload = cloudinary.uploader().upload(file.getBytes(), Map.of("public_id", "my_anime/" + id));
        return (String) upload.get("secure_url");
    }

    @Override
    public void deleteFile(String id) throws IOException {
        cloudinary.uploader().destroy(id, Map.of());
    }


    private boolean isAllowExtension(String extension) {
        return extension != null && extension.matches("^(jpg|jpeg|png)$");
    }

    private void checkValidFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_SIZE_INVALID);
        }

        String fileName = file.getOriginalFilename();
        String extension = null;

        if (fileName != null) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        if (!isAllowExtension(extension)) {
            throw new AppException(ErrorCode.FILE_EXTENSION_INVALID);
        }
    }
}
