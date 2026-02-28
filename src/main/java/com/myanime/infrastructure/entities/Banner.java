package com.myanime.infrastructure.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Banner {
    @Id
    private String id;
    private String description;
    private String imageUrl;
    private String name;
    private String nameImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
