package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.dto.request.GetHubPathReqDto;
import com.sparta.deliverypathservice.global.dto.response.GetHubPathResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "hub-path-service")
public interface HubPathClient {

    //허브 경로 검색 : 출발허브, 도착허브 -> 예상 거리, 시간
    @GetMapping("/v1/hub-paths/api")
    ApiResponse<GetHubPathResDto> getHubPath(@RequestParam UUID startHubId, @RequestParam UUID endHubId);
}
