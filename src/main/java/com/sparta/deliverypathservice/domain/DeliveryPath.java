package com.sparta.deliverypathservice.domain;

import com.sparta.deliverypathservice.global.entity.BaseEntity;
import jakarta.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryPathId;

    @Column(nullable = false)
    private UUID deliveryId;

    @Column(nullable = false)
    private int sequence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryPathState status;

    @Column(nullable = false)
    private UUID startHubId;

    @Column(nullable = false)
    private UUID endHubId;

    @Column(nullable = false)
    private int estimated_distance;

    @Column(nullable = false)
    private int estimated_time;

    private int actual_distance;
    private int actual_time;

    @Column(nullable = false)
    private Long hubDeliveryUserId;
}
