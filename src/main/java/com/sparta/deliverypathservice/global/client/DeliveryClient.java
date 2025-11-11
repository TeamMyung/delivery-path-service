package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.domain.DeliveryPathState;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.UUID;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @PatchMapping("/v1/deliveries/api/state")
    void updateDeliveryPathState(UUID deliveryId, DeliveryPathState state);
}
