package com.sparta.deliverypathservice.global.dto.request;

import com.sparta.deliverypathservice.global.domain.user.DeliveryType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GetNextDeliveryManagerIdReqDto {

    @NotNull
    private DeliveryType deliveryType;

    private UUID hubId;
}
