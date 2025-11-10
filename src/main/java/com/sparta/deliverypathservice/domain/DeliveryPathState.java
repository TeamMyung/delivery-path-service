package com.sparta.deliverypathservice.domain;

public enum DeliveryPathState {

    //허브 대기중, 허브 이동중, 허브 이동완료, 업체 배송 대기중, 업체 배송중, 업체 배송완료
    HUB_WAIT,
    HUB_MOVING,
    HUB_COMPLETE,
    VENDOR_WAIT,
    VENDOR_MOVING,
    VENDOR_COMPLETE,
    ;

    private String state;
}
