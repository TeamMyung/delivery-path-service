package com.sparta.deliverypathservice.service;

import com.sparta.deliverypathservice.domain.DeliveryPathState;
import com.sparta.deliverypathservice.dto.request.CreateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.request.UpdateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.request.getDeliveryPathListReqDto;
import com.sparta.deliverypathservice.dto.response.*;
import com.sparta.deliverypathservice.repository.DeliveryPathRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPathService {

    private final DeliveryPathRepository deliveryPathRepository;

    public CreateDeliveryPathResDto create(@Valid CreateDeliveryPathReqDto reqDto) {
        //허브 경로 검색 : 출발허브, 도착허브 -> 거리, 시간
        //허브 배송담당자 배정
        //배송담당자, 허브담당자에게 슬랙 메세지 전송
        //반환 : 순서, 출발허브, 도착허브, 예상거리, 예상시간, 현상태=허브대기중,  허브 배송담당자 id
        return null;
    }

    public Page<getDeliveryPathListResDto> getDeliveryPaths(getDeliveryPathListReqDto reqDto) {

        if(true) { //어드민
            //모든 항목 조회
          return null;
        }

        if(true) { //허브 관리자
            //삭제x && (출발허브=담당허브 || 도착허브=담당허브)
            return null;
        }

        if(true) { //허브배송담당자
            //삭제x && 배송담당자=본인
            return null;
        }

        //권한 없음
        return null;
    }

    public getDeliveryPathResDto getDeliveryPath(UUID id) {

        if(true) { //어드민
            //모든 항목 조회
            return null;
        }

        if(true) { //허브관리자
            //삭제x && (출발허브=담당허브 || 도착허브=담당허브)
            return null;
        }

        if(true) { //업체담당자
            //삭제x && vendorId=담당업체
            return null;
        }

        //권한 없음
        return null;
    }

    public UpdateDeliveryPathResDto updateDeliveryPath(UUID id, UpdateDeliveryPathReqDto reqDto) {

        return null;
    }

    public List<DeleteDeliveryPathResDto> deleteDeliveryPath(List<UUID> paths) {

        return null;
    }

    public UpdateDeliveryPathStateResDto updateDeliveryPathState(UUID id, DeliveryPathState state) {
        //이동중
        //허브이동완료 -> 실제 시간, 거리 계산
        //배송중
        //배송 완료 -> 수령인에게 슬랙 알림
        return null;
    }
}
