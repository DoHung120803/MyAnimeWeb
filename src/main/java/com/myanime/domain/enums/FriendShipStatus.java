package com.myanime.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FriendShipStatus {
    SENT("SENT", "Đã gửi lời mời"),
    WAITING("WAITING", "Đang chờ phản hồi"),
    ACCEPTED("ACCEPTED", "Đã chấp nhận"),
    REJECTED("REJECTED", "Đã từ chối"),
    NONE("NONE", "Không phải bạn bè"),
    SELF("SELF", "Bản thân");

    private final String code;
    private final String description;
}
