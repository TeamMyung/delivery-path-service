package com.sparta.deliverypathservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "p_delivery_paths")
@Table(name = "p_delivery_paths")
public class DeliveryPath extends BaseEntity {

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
}
