package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.dto.HubPathDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@FeignClient(name = "hub-path-service")
public interface HubPathClient {

    //허브 경로 검색 : 출발허브, 도착허브 -> 예상 거리, 시간
    @GetMapping("/v1/hub-paths/api/{id}")
    ApiResponse<HubPathDto> getHubPath(UUID startHubId, UUID endHubId);
}
