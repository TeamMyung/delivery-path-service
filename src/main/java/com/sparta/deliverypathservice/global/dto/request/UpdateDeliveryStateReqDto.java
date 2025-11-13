package com.sparta.deliverypathservice.global.dto.request;

import com.sparta.deliverypathservice.domain.DeliveryPathState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateDeliveryStateReqDto {

    private DeliveryPathState state;
}
