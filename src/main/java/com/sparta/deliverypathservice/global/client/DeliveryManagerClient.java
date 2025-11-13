package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.dto.request.GetCurrentDeliveryManagerIdReqDto;
import com.sparta.deliverypathservice.global.dto.request.GetNextDeliveryManagerIdReqDto;
import com.sparta.deliverypathservice.global.dto.response.GetCurrentDeliveryManagerIdResDto;
import com.sparta.deliverypathservice.global.dto.response.GetNextDeliveryManagerIdResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-manager-service")
public interface DeliveryManagerClient {

    @PostMapping("/v1/delivery-managers/rotation/current")
    ApiResponse<GetCurrentDeliveryManagerIdResDto> getFirstDeliveryManagerUserId(
            @RequestBody GetCurrentDeliveryManagerIdReqDto reqDto);

    //serial number순으로 정렬했을 때 주어진 userId 다음에 오는 레코드의 userId 반환
    @PostMapping("/v1/delivery-managers/rotation/next")
    ApiResponse<GetNextDeliveryManagerIdResDto> getNextDeliveryManagerUserId(
            @RequestBody GetNextDeliveryManagerIdReqDto reqDto);
}
