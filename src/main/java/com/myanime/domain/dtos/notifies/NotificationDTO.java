package com.myanime.domain.dtos.notifies;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NotificationDTO {
    private String receiver;
    private String title;
    private String message;
    private String url;         // link đính kèm nếu cần
    private Map<String, Object> metaData; // dữ liệu tuỳ chỉnh
}
