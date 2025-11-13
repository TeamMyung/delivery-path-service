package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.dto.response.GetSlackAccountIdResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/v1/users/{userId}/slack-account")
    ApiResponse<GetSlackAccountIdResDto> getSlackAccountId(@PathVariable Long userId);
}
