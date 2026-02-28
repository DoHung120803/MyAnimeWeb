package com.myanime.domain.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.dtos.MediaInfoDTO;
import com.myanime.domain.enums.FileExtensionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final long MAX_FILE_SIZE = 2097152;

    private final Cloudinary cloudinary;

    @Value("${media.image.resize.max-dimension}")
    private Integer resizeImageMaxDimension;

    @Value("${media.image.compress.max-size}")
    private Long compressImageMaxSize;

    @Value("${media.image.compress.quality}")
    private Float compressImageQuality;

    @Value("${media.image.resize.flag}")
    private Boolean resizeImageFlag;


    @Override
    public String uploadFile(String id, MultipartFile file) throws IOException, BadRequestException {
        checkValidFile(file);
        Map<?, ?> upload = cloudinary.uploader().upload(file.getBytes(), Map.of("public_id", "my_anime/" + id));
        return (String) upload.get("secure_url");
    }

    @Override
    public MediaInfoDTO uploadFile(MultipartFile file, String prefix) throws IOException, BadRequestException {
        checkValidFile(file);
        String resourceType = resolveResourceType(file.getContentType());
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

        FileExtensionType fileExtensionType = FileExtensionType.valueOf(extension.toUpperCase());

        byte[] processedBytes = file.getBytes();

        // Xử lý ảnh nếu là định dạng ảnh, nếu là định dạng khác thì giữ nguyên dữ liệu gốc
        if (fileExtensionType.isImage()) {
            processedBytes = processImage(file.getBytes());
            extension = "webp"; // Chuyển đổi định dạng ảnh sang webp sau khi nén
        }

        prefix = StringUtils.hasText(prefix) ? prefix + "/" : "";

        Map<?, ?> upload = cloudinary.uploader().upload(processedBytes, Map.of(
                "folder", "my_anime/" + prefix,
                "public_id",UUID.randomUUID() + "_" + fileName,
                "resource_type", resourceType,
                "format", extension
        ));

        return MediaInfoDTO.builder()
                .url((String) upload.get("secure_url"))
                .size(processedBytes.length * 1.0 / 1024) // Kích thước file sau khi xử lý, tính bằng KB
                .fileName(fileName + "." + extension)
                .type(fileExtensionType.getFileType())
                .build();
    }

    @Override
    public void deleteFile(String id) throws IOException {
        cloudinary.uploader().destroy(id, Map.of());
    }


    private boolean isAllowExtension(String extension) {
        return extension != null && FileExtensionType.isSupported(extension);
    }

    private void checkValidFile(MultipartFile file) throws BadRequestException {
        if (file.isEmpty()) {
            throw new BadRequestException("File không hợp lệ");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("Kích thước file vượt quá giới hạn cho phép (2MB)");
        }

        String fileName = file.getOriginalFilename();
        String extension = null;

        if (fileName != null) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        if (!isAllowExtension(extension)) {
            throw new BadRequestException("Định dạng file không được hỗ trợ");
        }
    }

    private byte[] processImage(byte[] originalBytes) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalBytes));

//      Resize ảnh nếu cần thiết
        if (Boolean.TRUE.equals(resizeImageFlag)) {
            originalImage = resizeKeepRatio(originalImage);
        }

        return compressImage(originalImage);
    }

    private BufferedImage resizeKeepRatio(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        if (width <= resizeImageMaxDimension && height <= resizeImageMaxDimension) {
            return original;
        }

        double scale = (double) resizeImageMaxDimension / Math.max(width, height);
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        Image tmp = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

    private byte[] compressImage(BufferedImage image) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "webp", baos);
            byte[] byteArray = baos.toByteArray();

            if (byteArray.length > compressImageMaxSize)
                throw new BadRequestException("Kích thước file sau khi nén vẫn vượt quá giới hạn cho phép (2MB)");

            return byteArray;
        } catch (Exception e) {
            log.error("Error compressing image", e);
            throw new IOException("Failed to compress image", e);
        }
    }

    private String resolveResourceType(String contentType) {

        if (contentType == null) {
            return "raw";
        }

        String mainType = contentType.split("/")[0];

        return switch (mainType) {
            case "image" -> "image";
            case "video" -> "video";
            case "audio" -> "video"; // Cloudinary xử lý audio qua video
            default -> "raw";
        };
    }
}
