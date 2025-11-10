package com.sparta.deliverypathservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateDeliveryPathStateResDto {

    private UUID deliveryPathId;
    private UUID deliveryId;
    private int sequence;
    private String status;
    private UUID startHubId;
    private UUID endHubId;
    private int estimated_distance;
    private int estimated_time;
    private int actual_distance;
    private int actual_time;
    private UUID hubDeliveryUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
