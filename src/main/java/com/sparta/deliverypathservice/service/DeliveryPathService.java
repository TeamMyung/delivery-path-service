package com.sparta.deliverypathservice.service;

import com.sparta.deliverypathservice.domain.DeliveryPath;
import com.sparta.deliverypathservice.domain.DeliveryPathState;
import com.sparta.deliverypathservice.dto.request.CreateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.request.GetDeliveryPathListReqDto;
import com.sparta.deliverypathservice.dto.request.UpdateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.response.*;
import com.sparta.deliverypathservice.global.client.DeliveryClient;
import com.sparta.deliverypathservice.global.client.HubPathClient;
import com.sparta.deliverypathservice.global.client.SlackClient;
import com.sparta.deliverypathservice.global.client.UserClient;
import com.sparta.deliverypathservice.global.domain.user.User;
import com.sparta.deliverypathservice.global.domain.user.UserRole;
import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.dto.UserDetailsDto;
import com.sparta.deliverypathservice.global.dto.request.GetHubPathReqDto;
import com.sparta.deliverypathservice.global.dto.request.SendMessageReqDto;
import com.sparta.deliverypathservice.global.dto.request.UpdateDeliveryStateReqDto;
import com.sparta.deliverypathservice.global.dto.response.GetHubPathResDto;
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
import java.util.Optional;
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
        GetHubPathResDto getHubPathResDto = searchHubPath(reqDto.getFirstHubId(), reqDto.getFinalHubId());

        // api - 허브 배송담당자 배정
        Long assignedDeliveryUserId = assignHubDeliveryManager();

        // TODO api - 배송담당자에게 슬랙 메세지 전송
//        sendNewDeliveryMessageToHubDeliveryManager(assignedDeliveryUserId);

        // 저장 : 순서, 출발허브, 도착허브, 예상거리, 예상시간, 현상태=허브대기중,  허브 배송담당자 id
        DeliveryPath entity = reqDto.toEntity();
        entity.setSequence(1);
        entity.setStatus(DeliveryPathState.HUB_WAIT);
        entity.setEstimated_distance(getHubPathResDto.getDistance());
        entity.setEstimated_time(getHubPathResDto.getDuration());
        entity.setHubDeliveryUserId(assignedDeliveryUserId);
        entity = deliveryPathRepository.save(entity);

        return CreateDeliveryPathResDto.toDto(entity);
    }

    public Page<GetDeliveryPathListResDto> getDeliveryPaths(UserRole role, Long userId, UUID hubId, UUID vendorId, GetDeliveryPathListReqDto reqDto) {

        Sort.Direction direction = reqDto.getPageable().isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, reqDto.getPageable().getSortBy());
        Pageable pageable = PageRequest.of(reqDto.getPageable().getPage(), reqDto.getPageable().getSize(), sort);

        //어드민 : 모든 항목 조회
        if(role.equals(UserRole.MASTER)) {
            Page<DeliveryPath> paths = deliveryPathRepository.findAll(pageable);
            return paths.map(GetDeliveryPathListResDto::new);
        }

        //허브 관리자 : 삭제x && (출발허브=담당허브 || 도착허브=담당허브)
        if(role.equals(UserRole.HUB_MANAGER) && vendorId == null) {
            Page<DeliveryPath> paths = deliveryPathRepository.findAllByStartHubIdOrEndHubIdAndDeletedAtIsNull(hubId, hubId, pageable);
            return paths.map(GetDeliveryPathListResDto::new);
        }

        //허브배송담당자 : 삭제x && 배송담당자=본인
        if(role.equals(UserRole.DELIVERY_MANAGER)) {
            Page<DeliveryPath> paths = deliveryPathRepository.findAllByHubDeliveryUserIdAndDeletedAtIsNull(userId, pageable);
            return paths.map(GetDeliveryPathListResDto::new);
        }

        throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, "getDeliveryPaths(%s): 권한없음".formatted(role.toString()));
    }

    public getDeliveryPathResDto getDeliveryPath(UserRole role, UUID hubId, UUID vendorId, UUID id) {

//        ApiResponse<User> apiResponse = userClient.getUser(token);
//        User user = apiResponse.getData();
//        UserRole role = user.getRole();

        //어드민 : 모든 항목에서 조회
        if(role.equals(UserRole.MASTER)) {
            DeliveryPath entity = deliveryPathRepository.findById(id).orElseThrow(() ->
                    new DeliveryPathException(ErrorCode.DELIVERY_PATH_NOT_FOUND, "배송 경로 상세 조회 : 대상 레코드를 찾을 수 없습니다")
            );
            return getDeliveryPathResDto.toDto(entity);
        }

        //허브 관리자 : 삭제x && (출발허브=담당허브 || 도착허브=담당허브)
        if(role.equals(UserRole.HUB_MANAGER) && vendorId == null) {
            DeliveryPath entity = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNullAndStartHubIdOrDeliveryPathIdAndDeletedAtIsNullAndEndHubId(id, hubId, id, hubId).orElseThrow(() ->
                    new DeliveryPathException(ErrorCode.DELIVERY_PATH_NOT_FOUND, "배송 경로 상세 조회 : 대상 레코드를 찾을 수 없습니다")
            );
            return getDeliveryPathResDto.toDto(entity);
        }

        //업체담당자 : 삭제x && vendorId=담당업체(배송에서 확인)
        if(role.equals(UserRole.VENDOR_MANAGER)) {
            DeliveryPath entity = deliveryPathRepository.findByDeliveryPathIdAndDeletedAtIsNull(id).orElseThrow(() ->
                    new DeliveryPathException(ErrorCode.DELIVERY_PATH_NOT_FOUND, "배송 경로 상세 조회 : 대상 레코드를 찾을 수 없습니다")
            );
            return getDeliveryPathResDto.toDto(entity);
        }

        //권한 없음
        throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, "getDeliveryPaths(%s): 권한없음".formatted(role.toString()));
    }

    @Transactional
    public UpdateDeliveryPathResDto updateDeliveryPath(UUID id, UpdateDeliveryPathReqDto reqDto) {
        DeliveryPath entity = deliveryPathRepository.findById(id).orElseThrow(() ->
                new DeliveryPathException(ErrorCode.DELIVERY_PATH_NOT_FOUND, "배송 경로 수정 : 대상 레코드를 찾을 수 없습니다")
        );

        entity.setStatus(reqDto.getStatus());
        entity.setHubDeliveryUserId(reqDto.getHubDeliveryUserId());

        return UpdateDeliveryPathResDto.toDto(entity);
    }

    @Transactional
    public List<DeleteDeliveryPathResDto> deleteDeliveryPath(List<UUID> paths, Long userId) {
        List<DeliveryPath> entities = new ArrayList<>();

        paths.forEach(id -> {
            DeliveryPath entity = deliveryPathRepository.findById(id).orElseThrow( () ->
                new DeliveryPathException(ErrorCode.DELIVERY_PATH_NOT_FOUND, "배송 경로 삭제 : 대상 레코드를 찾을 수 없습니다")
            );
            entity.delete(userId);
            entities.add(entity);
        });

        return entities.stream().map(DeleteDeliveryPathResDto::new).collect(Collectors.toList());
    }

    @Transactional
    public UpdateDeliveryPathStateResDto updateDeliveryPathState(UUID id, DeliveryPathState state) {

        DeliveryPath entity = deliveryPathRepository.findById(id).orElseThrow( () ->
                new DeliveryPathException(ErrorCode.DELIVERY_PATH_NOT_FOUND, "배송 상태 변경 : 대상 레코드를 찾을 수 없습니다")
        );

        if(state.equals(DeliveryPathState.HUB_COMPLETE)) {
            //TODO 실제 시간, 거리 계산

        }

        // api - 배송 상태 변경
        changeDeliveryState(id, state);

        entity.setStatus(state);

        return UpdateDeliveryPathStateResDto.toDto(entity);
    }



    private GetHubPathResDto searchHubPath(UUID sHubId, UUID eHubId) {
        GetHubPathReqDto getHubPathReqDto = new GetHubPathReqDto(sHubId, eHubId);
        GetHubPathResDto getHubPathResDto = hubPathClient.getHubPath(sHubId, eHubId).getData();

        if(getHubPathResDto == null) {
            //조회된 허브 경로 없음
        }

        return getHubPathResDto;
    }

    private Long assignHubDeliveryManager() {
        Long nextDeliveryManagerUserId = null;

        Optional<DeliveryPath> lastDeliveryPath = deliveryPathRepository.findTopByOrderByCreatedAtDesc();
        if (lastDeliveryPath.isPresent()) {
            Long lastestHubDeliveryUserId = lastDeliveryPath.get().getHubDeliveryUserId();
            nextDeliveryManagerUserId = deliveryClient.getNextDeliveryManagerUserId(lastestHubDeliveryUserId).getData();
        } else {
            nextDeliveryManagerUserId = deliveryClient.getFirstDeliveryManagerUserId().getData();
        }
        if(nextDeliveryManagerUserId == null){
            //배송 담당자 없음
        }
        if(nextDeliveryManagerUserId == null){
            //해당 순번의 배송 담당자 없음
        }

        return nextDeliveryManagerUserId;
    }

    private void sendNewDeliveryMessageToHubDeliveryManager(Long assignedDeliveryUserId) {
        String slackId = userClient.getSlackAccountId(assignedDeliveryUserId).getData();
        if(slackId == null) {
            //조회된 값 없음
        }

        SendMessageReqDto sendMessageReqDto = new SendMessageReqDto();
        sendMessageReqDto.newDeliveryMessageToHubDeliveryManager(slackId);
        slackClient.sendMessage(sendMessageReqDto);
    }

    private void changeDeliveryState(UUID id, DeliveryPathState state) {
        UpdateDeliveryStateReqDto updateDeliveryStateReqDto = new UpdateDeliveryStateReqDto(id, state);
        deliveryClient.updateDeliveryState(updateDeliveryStateReqDto);
    }
}
