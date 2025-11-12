package com.sparta.deliverypathservice.controller;

import com.sparta.deliverypathservice.domain.DeliveryPathState;
import com.sparta.deliverypathservice.dto.request.CreateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.request.GetDeliveryPathListReqDto;
import com.sparta.deliverypathservice.dto.request.UpdateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.response.*;
import com.sparta.deliverypathservice.global.domain.user.DeliveryType;
import com.sparta.deliverypathservice.global.domain.user.UserRole;
import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.global.exception.DeliveryPathException;
import com.sparta.deliverypathservice.global.exception.ErrorCode;
import com.sparta.deliverypathservice.service.DeliveryPathService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/v1/delivery-paths")
@RestController
@RequiredArgsConstructor
public class DeliveryPathController {

    private static final String ROLE_HEADER = "role";
    private static final String USER_ID_HEADER = "user_id";
    private static final String HUB_ID_HEADER = "hub_id";
    private static final String VENDOR_ID_HEADER = "vendor_id";
    private static final String DELIVERY_TYPE_HEADER = "delivery_type";

    private final DeliveryPathService deliveryPathService;

    @Operation(summary = "배송 경로 생성", description = "어드민이 배송 경로를 생성하는 API 입니다.")
    @PostMapping
    public ApiResponse<CreateDeliveryPathResDto> create(
            @RequestBody @Valid CreateDeliveryPathReqDto reqDto,
            @RequestHeader(ROLE_HEADER) UserRole role
    ) {

        // admin 권한 확인
        checkAdmin(role, "배송 경로 생성");

        CreateDeliveryPathResDto data = deliveryPathService.create(reqDto);
        return new ApiResponse<>(data);
    }

    @Operation(summary = "배송 경로 리스트 조회", description = "배송 경로 리스트를 조회하는 API 입니다.")
    @GetMapping
    public ApiResponse<Page<GetDeliveryPathListResDto>> getDeliveryPaths(
            @RequestBody GetDeliveryPathListReqDto reqDto,
            @RequestHeader(ROLE_HEADER) UserRole role,
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestHeader(HUB_ID_HEADER) UUID hubId,
            @RequestHeader(VENDOR_ID_HEADER) UUID vendorId,
            @RequestHeader(DELIVERY_TYPE_HEADER) DeliveryType deliveryType

    ) {
        // 권한 확인 : 어드민, 허브관리자, 허브배송담당자
        if(role != UserRole.MASTER && role != UserRole.HUB_MANAGER && role != UserRole.DELIVERY_MANAGER) {
            throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, "배송 경로 리스트 조회: 권한 없음");
        }
        if(role == UserRole.DELIVERY_MANAGER && deliveryType != DeliveryType.HUB_TO_HUB) {
            throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, "배송 경로 리스트 조회: 권한 없음");
        }

        Page<GetDeliveryPathListResDto> data = deliveryPathService.getDeliveryPaths(role, userId, hubId, vendorId, reqDto);
        return new ApiResponse<>(data);
    }

    @Operation(summary = "배송 경로 상세 조회", description = "특정 배송 경로의 상세 정보를 조회하는 API 입니다.")
    @GetMapping("/{id}")
    public ApiResponse<getDeliveryPathResDto> getDeliveryPath(
            @PathVariable UUID id,
            @RequestHeader(ROLE_HEADER) UserRole role,
            @RequestHeader(HUB_ID_HEADER) UUID hubId,
            @RequestHeader(VENDOR_ID_HEADER) UUID vendorId
    ) {
        // 권한 확인 : 어드민, 허브관리자, 업체담당자
        if(role != UserRole.MASTER && role != UserRole.HUB_MANAGER && role != UserRole.VENDOR_MANAGER) {
            throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, "배송 경로 상세 조회: 권한 없음");
        }

        getDeliveryPathResDto data = deliveryPathService.getDeliveryPath(role, hubId, vendorId, id);
        return new ApiResponse<>(data);
    }

    @Operation(summary = "배송 경로 수정", description = "특정 배송 경로를 수정하는 API 입니다.")
    @PutMapping("/{id}")
    public ApiResponse<UpdateDeliveryPathResDto> updateDeliveryPath(
            @PathVariable UUID id,
            @RequestBody UpdateDeliveryPathReqDto reqDto,
            @RequestHeader(ROLE_HEADER) UserRole role
    ) {
        // admin 권한 확인
        checkAdmin(role, "배송 경로 수정");

        UpdateDeliveryPathResDto data = deliveryPathService.updateDeliveryPath(id, reqDto);
        return new ApiResponse<>(data);
    }

    @Operation(summary = "배송 경로 삭제", description = "한 개 이상의 배송 경로를 삭제하는 API 입니다.")
    @DeleteMapping
    public ApiResponse<List<DeleteDeliveryPathResDto>> deleteDeliveryPath(
            @RequestBody List<UUID> paths,
            @RequestHeader(ROLE_HEADER) UserRole role,
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        // admin 권한 확인
        checkAdmin(role, "배송 경로 삭제");

        List<DeleteDeliveryPathResDto> data = deliveryPathService.deleteDeliveryPath(paths, userId);
        return new ApiResponse<>(data);
    }

    @Operation(summary = "배송 경로 상태 변경", description = "특정 배송 경로의 상태를 변경하는 API 입니다.")
    @PatchMapping("/{id}")
    public ApiResponse<UpdateDeliveryPathStateResDto> updateDeliveryPathState(
            @PathVariable UUID id,
            @RequestParam DeliveryPathState state,
            @RequestHeader(ROLE_HEADER) UserRole role,
            @RequestHeader(DELIVERY_TYPE_HEADER) DeliveryType deliveryType
    ) {
        // 권한 확인 : 어드민, 허브배송담당자
        if(role != UserRole.MASTER && role != UserRole.DELIVERY_MANAGER) {
            throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, "배송 경로 상태 변경: 권한 없음");
        }
        if(role == UserRole.DELIVERY_MANAGER && deliveryType != DeliveryType.HUB_TO_HUB) {
            throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, "배송 경로 상태 변경: 권한 없음");
        }

        UpdateDeliveryPathStateResDto data = deliveryPathService.updateDeliveryPathState(id, state);
        return new ApiResponse<>(data);
    }

    private void checkAdmin(UserRole role, String method) {
        if(role != UserRole.MASTER) {
            throw new DeliveryPathException(ErrorCode.DELIVERY_PATH_FORBIDDEN, method + " : 어드민 권한 없음");
        }
    }
}
