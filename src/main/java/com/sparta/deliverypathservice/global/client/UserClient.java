package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.dto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service")
public interface UserClient {

    //유저 정보 가져오기
    @PostMapping("/v1/internal/authz/check")
    ApiResponse<UserDetailsDto> getUser(String token);

    @GetMapping("/v1/users/{id}")
    ApiResponse<UserDetailsDto> getUser(Long userId);

    //허브 배송담당자 배정
//    @PostMapping("/v1/users/api/assign-hub-delivery-manager")
//    ApiResponse<Long> assignHubDeliveryManager();

    @GetMapping("/v1/users/delivery-manager")
    ApiResponse<Long> getDeliveryManager(Integer serialNum);

    @GetMapping("/v1/users/first-delivery-manager")
    ApiResponse<Integer> getFirstDeliveryManager();

    @GetMapping("/v1/users/next-delivery-manager")
    ApiResponse<Integer> getNextDeliveryManager(UserDetailsDto latestUser);


//    @PostMapping("/v1/users/api/assign-vendor-delivery-manager")
//    ApiResponse<Long> assignVendorDeliveryManager();
}
