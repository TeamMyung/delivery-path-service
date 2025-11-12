package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.global.domain.user.DeliveryManager;
import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.dto.UserDetailsDto;
import com.sparta.deliverypathservice.global.dto.request.UpdateDeliveryStateReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    //배송 상태 변경
    @PatchMapping("/v1/deliveries/api/state")
    void updateDeliveryState(UpdateDeliveryStateReqDto resDto);

    //serial number순으로 정렬했을 때 첫번째 허브배송담당자의 userId 반환
    @GetMapping("/v1/deliveries/api/first-delivery-manager")
    ApiResponse<Long> getFirstDeliveryManagerUserId();

    //serial number순으로 정렬했을 때 주어진 userId 다음에 오는 레코드의 userId 반환
    @PostMapping("/v1/deliveries/api/next-delivery-manager")
    ApiResponse<Long> getNextDeliveryManagerUserId(Long userId);
}
