package com.sparta.deliverypathservice.global.dto.request;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageReqDto {

    private String slackId;
    private String message;
    private String channelId;

    public void newDeliveryMessageToHubDeliveryManager(String slackId) {
        this.slackId = slackId;
        this.message = "새로운 배송 요청이 들어왔습니다";
        this.channelId = null;
    }
}
