package com.myanime.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
//    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
//
//    ANIME_EXISTED(1001, "Anime existed!", HttpStatus.BAD_REQUEST),
//    ANIME_NAME_INVALID(1002, "Anime's name must be at least 3 character!", HttpStatus.BAD_REQUEST),
//    ANIME_IFRAME_INVALID(1003, "Anime's iframe must not be empty", HttpStatus.BAD_REQUEST),
//    ANIME_THUMBNAIL_INVALID(1004, "Anime's thumbnail must not be empty", HttpStatus.BAD_REQUEST),
//    ANIME_NOT_FOUND(1005, "Anime not found!", HttpStatus.NOT_FOUND),
//
//    USER_EXISTED(1001, "User existed!", HttpStatus.BAD_REQUEST),
//    USERNAME_INVALID(1002, "User's name must be at least 3 characters!", HttpStatus.BAD_REQUEST),
//    PASSWORD_INVALID(1003, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
//    USER_NOT_FOUND(1004, "User not found!", HttpStatus.NOT_FOUND),
//
//    UNAUTHENTICATED(1006, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
//    UNAUTHORIZED(1007, "You do not have permission!", HttpStatus.FORBIDDEN),
//
//    PERMISSION_NOT_FOUND(1005, "Permission not found!", HttpStatus.NOT_FOUND),
//
//    INVALID_KEY(1005, "Invalid message key", HttpStatus.BAD_REQUEST),
//
//    LIST_EMPTY(1006, "List is empty", HttpStatus.BAD_REQUEST),
//
//    REQUEST_BODY_EMPTY(1007, "Request body is empty", HttpStatus.BAD_REQUEST),
//
//    ELASTICSEARCH_CONNECTION_ERROR(1008, "Cannot connect to Elasticsearch!", HttpStatus.INTERNAL_SERVER_ERROR),
//    REDIS_CONNECTION_ERROR(1008, "Cannot connect to Redis!", HttpStatus.INTERNAL_SERVER_ERROR),
//    ;

    UNCATEGORIZED_EXCEPTION(9999, "Có lỗi xảy ra, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR),

    ANIME_EXISTED(1001, "Anime đã tồn tại!", HttpStatus.BAD_REQUEST),
    ANIME_NAME_INVALID(1002, "Tên anime phải có ít nhất 3 ký tự!", HttpStatus.BAD_REQUEST),
    ANIME_IFRAME_INVALID(1003, "Iframe của anime không được để trống", HttpStatus.BAD_REQUEST),
    ANIME_THUMBNAIL_INVALID(1004, "Thumbnail của anime không được để trống", HttpStatus.BAD_REQUEST),
    ANIME_NOT_FOUND(1005, "Không tìm thấy anime!", HttpStatus.NOT_FOUND),

    GENRE_EXISTED(1021, "Thể loại đã tồn tại!", HttpStatus.BAD_REQUEST),
    GENRE_NOT_FOUND(1022, "Không tìm thấy thể loại!", HttpStatus.NOT_FOUND),

    USER_EXISTED(1001, "Người dùng đã tồn tại!", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "Tên người dùng phải có ít nhất 3 ký tự!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Mật khẩu phải có ít nhất 6 ký tự", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "Không tìm thấy người dùng!", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1006, "Chưa xác thực!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền!", HttpStatus.FORBIDDEN),

    PERMISSION_NOT_FOUND(1005, "Không tìm thấy quyền!", HttpStatus.NOT_FOUND),

    INVALID_KEY(1005, "Khóa tin nhắn không hợp lệ", HttpStatus.BAD_REQUEST),

    LIST_EMPTY(1006, "Danh sách trống", HttpStatus.BAD_REQUEST),

    REQUEST_BODY_EMPTY(1007, "Request body không được trống", HttpStatus.BAD_REQUEST),

    ELASTICSEARCH_CONNECTION_ERROR(1008, "Không thể kết nối đến Elasticsearch!", HttpStatus.INTERNAL_SERVER_ERROR),
    REDIS_CONNECTION_ERROR(1008, "Không thể kết nối đến Redis!", HttpStatus.INTERNAL_SERVER_ERROR),

    FILE_EMPTY(1009, "File không được trống", HttpStatus.BAD_REQUEST),
    FILE_SIZE_INVALID(1010, "File không được vượt quá 2MB", HttpStatus.BAD_REQUEST),
    FILE_EXTENSION_INVALID(1011, "Chỉ được phép tải lên các file có định dạng jpg, png, gif, bmp", HttpStatus.BAD_REQUEST),

    MISSING_REQUEST_PARAM(1012, "Thiếu tham số yêu cầu", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode (int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;
}
