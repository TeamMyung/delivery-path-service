package com.sparta.deliverypathservice.global.dto;

import com.sparta.deliverypathservice.global.domain.user.DeliveryManager;
import com.sparta.deliverypathservice.global.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDetailsDto {

    private final User user;
    private final DeliveryManager deliveryManager;
}
