package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.global.domain.user.User;
import com.sparta.deliverypathservice.global.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service")
public interface UserClient {

    //User 조회
    @PostMapping("/v1/internal/authz/check")
    ApiResponse<User> getUser(String token);

    @GetMapping("/v1/users/{userId}")
    ApiResponse<String> getSlackAccountId(@PathVariable Long userId);
}
