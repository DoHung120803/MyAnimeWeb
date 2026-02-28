package com.myanime.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
public enum FileExtensionType {
    PNG("png", (short) 1, "image"),
    JPG("jpg", (short) 1, "image"),
    JPEG("jpeg", (short) 1, "image"),
    WEBP("webp", (short) 1, "image"),
    PDF("pdf", (short) 4, "file"),
    DOC("doc", (short) 4, "file"),
    DOCX("docx", (short) 4, "file"),
    XLSX("xlsx", (short) 4, "file"),
    PPTX("pptx", (short) 4, "file"),
    TXT("txt", (short) 4, "file"),
    ZIP("zip", (short) 4, "file"),
    RAR("rar", (short) 4, "file"),
    MP4("mp4", (short) 2, "video"),
    MP3("mp3", (short) 3, "audio");

    private final String extension;
    private final Short fileType;
    private final String name;

    public static boolean isSupported(String extension) {
        for (FileExtensionType type : FileExtensionType.values()) {
            if (type.getExtension().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    public boolean isImage() {
        return this == PNG || this == JPG || this == JPEG || this == WEBP;
    }
}
