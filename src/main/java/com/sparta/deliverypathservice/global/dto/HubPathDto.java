package com.sparta.deliverypathservice.global.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class HubPathDto {

    UUID startHubId;
    UUID endHubId;
    int duration;
    int distance;
}
