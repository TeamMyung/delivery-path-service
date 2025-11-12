package com.sparta.deliverypathservice.global.client;

import com.sparta.deliverypathservice.global.dto.request.SendMessageReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "slack-service")
public interface SlackClient {

    //배송담당자에게 '배송 생성됨' 슬랙 메세지 전송
    @PostMapping("/v1/slacks")
    void sendMessage(SendMessageReqDto reqDto);
}
