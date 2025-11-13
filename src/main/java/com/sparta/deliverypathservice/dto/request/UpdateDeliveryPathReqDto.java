package com.sparta.deliverypathservice.dto.request;

import com.sparta.deliverypathservice.domain.DeliveryPathState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDeliveryPathReqDto {

    private DeliveryPathState status;
    private Long hubDeliveryUserId;
}
