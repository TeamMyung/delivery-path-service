package com.sparta.deliverypathservice.dto.response;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import com.sparta.deliverypathservice.domain.DeliveryPathState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class getDeliveryPathResDto {

    private UUID deliveryPathId;
    private UUID deliveryId;
    private int sequence;
    private DeliveryPathState status;
    private UUID startHubId;
    private UUID endHubId;
    private int estimated_distance;
    private int estimated_time;
    private int actual_distance;
    private int actual_time;
    private Long hubDeliveryUserId;

    public static getDeliveryPathResDto toDto(DeliveryPath entity) {
        return getDeliveryPathResDto.builder()
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
                .build();
    }
}
