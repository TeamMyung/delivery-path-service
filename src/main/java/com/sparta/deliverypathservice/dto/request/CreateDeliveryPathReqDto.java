package com.sparta.deliverypathservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

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
    }

    @AssertTrue
    public boolean isExistHub() {
        //TODO validation : 존재하는 허브인지
    }

    @AssertTrue
    public boolean isExistHub() {
        //TODO validation : 배송과 허브 매치가 맞는지
    }
}
