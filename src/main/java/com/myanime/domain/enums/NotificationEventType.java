package com.myanime.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationEventType {
    FRIEND_REQUEST("FRIEND_REQUEST", "Lời mời kết bạn"),
    ACCEPT_FRIEND("ACCEPT_FRIEND", "Chấp nhận lời mời kết bạn"),
    REJECT_FRIEND("REJECT_FRIEND", "Từ chối lời mời kết bạn");

    private final String code;
    private final String description;

}

