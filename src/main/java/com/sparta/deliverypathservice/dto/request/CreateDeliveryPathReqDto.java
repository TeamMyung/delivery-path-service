package com.sparta.deliverypathservice.dto.request;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDeliveryPathReqDto {

    @NotNull
    private UUID deliveryId;

    @NotNull
    private UUID firstHubId;

    @NotNull
    private UUID finalHubId;

    @AssertTrue
    public boolean isExistDelivery() {
        //TODO validation : 존재하는 배송인지
        return true;
    }

    @AssertTrue
    public boolean isExistHub() {
        //TODO validation : 존재하는 허브인지
        return true;
    }

    @AssertTrue
    public boolean isMatch() {
        //TODO validation : 배송과 허브 매치가 맞는지
        return true;
    }

    public DeliveryPath toEntity() {
        return DeliveryPath.builder()
                .deliveryId(deliveryId)
                .startHubId(firstHubId)
                .endHubId(finalHubId)
                .build();
    }
}
