package com.sparta.deliverypathservice.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "slack-service")
public interface SlackClient {

    //배송담당자에게 '배송 생성됨' 슬랙 메세지 전송
    @PostMapping("/v1/slacks")
    void sendMessage(String slackId, String text);

    //허브담당자에게 '배송 생성됨' 슬랙 메세지(ai) 전송
//    @PostMapping("/v1/slacks/api/send-message")
//    void sendMessage(UUID finalHubId, String text);

    //수령인에게 '배송 완료' 슬랙 알림
}
