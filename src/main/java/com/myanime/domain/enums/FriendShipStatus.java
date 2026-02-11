package com.myanime.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FriendShipStatus {
    SENT("SENT", "Đã gửi lời mời"),
    ACCEPTED("ACCEPTED", "Đã chấp nhận"),
    REJECTED("REJECTED", "Đã từ chối");

    private final String code;
    private final String description;
}
