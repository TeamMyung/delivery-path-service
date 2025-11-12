package com.sparta.deliverypathservice.global.dto.request;

import com.sparta.deliverypathservice.domain.DeliveryPathState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UpdateDeliveryStateReqDto {

    private UUID deliveryId;
    private DeliveryPathState state;
}
