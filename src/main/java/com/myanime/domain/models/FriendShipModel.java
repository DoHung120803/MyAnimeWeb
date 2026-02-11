package com.myanime.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendShipModel {
    private String id;
    private String lowUserId;
    private String highUserId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
