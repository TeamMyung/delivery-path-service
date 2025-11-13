package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.dto.request.UpdateDeliveryStateReqDto;
import com.sparta.deliverypathservice.global.dto.response.UpdateDeliveryStatusResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    //배송 상태 변경
    @PatchMapping("/v1/deliveries/{deliveryId}/status")
    ApiResponse<UpdateDeliveryStatusResDto> updateDeliveryState(
            @PathVariable UUID deliveryId, UpdateDeliveryStateReqDto reqDto);
}
