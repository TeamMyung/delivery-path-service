package com.sparta.deliverypathservice.global.domain.user;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryManager {

    private Long deliveryManagerId;

    private User user;

    private DeliveryType type;

    private UUID hubId;

    private Integer serialNumber;
}
