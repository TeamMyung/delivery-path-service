package com.sparta.deliverypathservice.service;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import com.sparta.deliverypathservice.domain.DeliveryPathState;
import com.sparta.deliverypathservice.global.domain.user.User;
import com.sparta.deliverypathservice.global.domain.user.UserRole;
import com.sparta.deliverypathservice.dto.request.CreateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.request.GetDeliveryPathListReqDto;
import com.sparta.deliverypathservice.dto.request.UpdateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.response.*;
import com.sparta.deliverypathservice.global.client.DeliveryClient;
import com.sparta.deliverypathservice.global.client.HubPathClient;
import com.sparta.deliverypathservice.global.client.SlackClient;
import com.sparta.deliverypathservice.global.client.UserClient;
import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.dto.HubPathDto;
import com.sparta.deliverypathservice.global.dto.UserDetailsDto;
import com.sparta.deliverypathservice.global.exception.DeliveryPathException;
import com.sparta.deliverypathservice.global.exception.ErrorCode;
import com.sparta.deliverypathservice.repository.DeliveryPathRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryPathService {

    private final DeliveryPathRepository deliveryPathRepository;
    private final UserClient userClient;
    private final HubPathClient hubPathClient;
    private final SlackClient slackClient;
    private final DeliveryClient deliveryClient;

    public CreateDeliveryPathResDto create(@Valid CreateDeliveryPathReqDto reqDto) {

        // api - 허브 경로 검색 : 출발허브, 도착허브 -> 예상 거리, 시간
        HubPathDto hubPathDto = hubPathClient.getHubPath(reqDto.getFirstHubId(), reqDto.getFinalHubId()).getData();

        // api - 허브 배송담당자 배정
        Long assignedDelivery = userClient.assignHubDeliveryManager().getData();

        // api - 배송담당자, 허브담당자에게 슬랙 메세지 전송
        slackClient.sendMessage(assignedDelivery, "새로운 배송 요청이 들어왔습니다");

        // 반환 : 순서, 출발허브, 도착허브, 예상거리, 예상시간, 현상태=허브대기중,  허브 배송담당자 id
        DeliveryPath entity = reqDto.toEntity();
        entity.setSequence(1);
        entity.setStatus(DeliveryPathState.HUB_WAIT);
        entity.setEstimated_distance(hubPathDto.getDistance());
        entity.setEstimated_time(hubPathDto.getDuration());
        entity.setHubDeliveryUserId(assignedDelivery);

        entity = deliveryPathRepository.save(entity);
        return CreateDeliveryPathResDto.toDto(entity);
    }

    public Page<GetDeliveryPathListResDto> getDeliveryPaths(String token, GetDeliveryPathListReqDto reqDto) {

        ApiResponse<UserDetailsDto> apiResponse = userClient.getUser(token);
        User user = apiResponse.getData().getUser();
        UserRole role = user.getRole();

        Sort.Direction direction = reqDto.getPageable().isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, reqDto.getPageable().getSortBy());
        Pageable pageable = PageRequest.of(reqDto.getPageable().getPage(), reqDto.getPageable().getSize(), sort);

        if(role.equals(UserRole.MASTER)) { //어드민
            //모든 항목 조회
            Page<DeliveryPath> paths = deliveryPathRepository.findAll(pageable);
            return paths.map(GetDeliveryPathListResDto::new);
        }

        if(role.equals(UserRole.HUB_MANAGER) && user.getVendorId() == null) { //허브 관리자
            //삭제x && (출발허브=담당허브 || 도착허브=담당허브)
            UUID hubId = user.getHubId();
            Page<DeliveryPath> paths = deliveryPathRepository.findAllByStartHubIdOrEndHubIdAndDeletedAtIsNull(hubId, hubId, pageable);
            return paths.map(GetDeliveryPathListResDto::new);
        }

        if(role.equals(UserRole.DELIVERY_MANAGER)) { //허브배송담당자
            //삭제x && 배송담당자=본인
            Long userId = user.getUserId();
            Page<DeliveryPath> paths = deliveryPathRepository.findAllByHubDeliveryUserIdAndDeletedAtIsNull(userId, pageable);
            return paths.map(GetDeliveryPathListResDto::new);
        }

        throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, "getDeliveryPaths(%s): 권한없음".formatted(role.toString()));
    }

    public getDeliveryPathResDto getDeliveryPath(String token, UUID id) {

        ApiResponse<UserDetailsDto> apiResponse = userClient.getUser(token);
        User user = apiResponse.getData().getUser();
        UserRole role = user.getRole();

        if(role.equals(UserRole.MASTER)) { //어드민
            //모든 항목에서 조회
            DeliveryPath entity = deliveryPathRepository.findById(id).orElseThrow();
            return getDeliveryPathResDto.toDto(entity);
        }

        if(role.equals(UserRole.HUB_MANAGER) && user.getVendorId() == null) { //허브 관리자
            //삭제x && (출발허브=담당허브 || 도착허브=담당허브)
            UUID hubId = user.getHubId();
            DeliveryPath entity = deliveryPathRepository.findByStartHubIdOrEndHubIdAndDeletedAtIsNullAndId(hubId, hubId, id).orElseThrow();
            return getDeliveryPathResDto.toDto(entity);
        }

        if(role.equals(UserRole.VENDOR_MANAGER)) { //업체담당자
            //삭제x && vendorId=담당업체(배송에서 확인)
            DeliveryPath entity = deliveryPathRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();
            return getDeliveryPathResDto.toDto(entity);
        }

        //권한 없음
        throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, "getDeliveryPaths(%s): 권한없음".formatted(role.toString()));
    }

    @Transactional
    public UpdateDeliveryPathResDto updateDeliveryPath(UUID id, UpdateDeliveryPathReqDto reqDto) {
        DeliveryPath entity = deliveryPathRepository.findById(id).orElseThrow();
        entity.setStatus(reqDto.getStatus());
        entity.setHubDeliveryUserId(reqDto.getHubDeliveryUserId());

        return UpdateDeliveryPathResDto.toDto(entity);
    }

    @Transactional
    public List<DeleteDeliveryPathResDto> deleteDeliveryPath(List<UUID> paths, String token) {
        List<DeliveryPath> entities = new ArrayList<>();
        ApiResponse<UserDetailsDto> apiResponse = userClient.getUser(token);
        User user = apiResponse.getData().getUser();
        paths.forEach(id -> {
            DeliveryPath entity = deliveryPathRepository.findById(id).orElseThrow();
            entity.delete(user.getUserId());
            entities.add(entity);
        });
        return entities.stream().map(DeleteDeliveryPathResDto::new).collect(Collectors.toList());
    }

    @Transactional
    public UpdateDeliveryPathStateResDto updateDeliveryPathState(UUID id, DeliveryPathState state) {
        DeliveryPath entity = deliveryPathRepository.findById(id).orElseThrow();

        if(state.equals(DeliveryPathState.HUB_COMPLETE)) {
            //TODO 실제 시간, 거리 계산

        }

        // api - 배송 상태 변경
        deliveryClient.updateDeliveryPathState(id, state);

        entity.setStatus(state);

        return UpdateDeliveryPathStateResDto.toDto(entity);
    }
}
