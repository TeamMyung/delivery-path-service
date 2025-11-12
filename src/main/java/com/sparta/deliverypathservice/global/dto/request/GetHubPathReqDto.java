package com.sparta.deliverypathservice.global.dto.request;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GetHubPathReqDto {

    UUID startHubId;
    UUID endHubId;
}
