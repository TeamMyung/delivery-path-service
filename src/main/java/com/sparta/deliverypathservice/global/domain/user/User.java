package com.sparta.deliverypathservice.global.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long userId;

    private String username;

    private String password;

    private String name;

    private String email;

    private UserRole role;

    private UserStatus status;

    private String slackAccountId;

    private UUID hubId;

    private UUID vendorId;

    private Boolean isDeliveryManager;
}
