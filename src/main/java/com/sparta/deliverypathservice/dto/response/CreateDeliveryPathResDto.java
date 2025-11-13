package com.sparta.deliverypathservice.dto.response;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import com.sparta.deliverypathservice.domain.DeliveryPathState;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDeliveryPathResDto {

    @NotNull
    private UUID deliveryPathId;

    private UUID deliveryId;

    @NotNull
    private int sequence;

    @NotNull
    private DeliveryPathState status;

    @NotNull
    private UUID startHubId;

    @NotNull
    private UUID endHubId;

    @NotNull
    private int estimated_distance;

    @NotNull
    private int estimated_time;

    private int actual_distance;
    private int actual_time;

    @NotNull
    private Long hubDeliveryUserId;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    public static CreateDeliveryPathResDto toDto(DeliveryPath entity) {
        return CreateDeliveryPathResDto.builder()
                .deliveryPathId(entity.getDeliveryPathId())
                .deliveryId(entity.getDeliveryId())
                .sequence(entity.getSequence())
                .status(entity.getStatus())
                .startHubId(entity.getStartHubId())
                .endHubId(entity.getEndHubId())
                .estimated_distance(entity.getEstimated_distance())
                .estimated_time(entity.getEstimated_time())
                .actual_distance(entity.getActual_distance())
                .actual_time(entity.getActual_time())
                .hubDeliveryUserId(entity.getHubDeliveryUserId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public DeliveryPath toEntity() {
        return DeliveryPath.builder()
                .deliveryPathId(deliveryPathId)
                .deliveryId(deliveryId)
                .sequence(sequence)
                .status(status)
                .startHubId(startHubId)
                .endHubId(endHubId)
                .estimated_distance(estimated_distance)
                .estimated_time(estimated_time)
                .actual_distance(actual_distance)
                .actual_time(actual_time)
                .hubDeliveryUserId(hubDeliveryUserId)
                .build();
    }
}
