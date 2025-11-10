package com.sparta.deliverypathservice.controller;

import com.sparta.deliverypathservice.domain.DeliveryPathState;
import com.sparta.deliverypathservice.dto.request.CreateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.request.UpdateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.request.getDeliveryPathListReqDto;
import com.sparta.deliverypathservice.dto.response.*;
import com.sparta.deliverypathservice.global.config.ApiResponse;
import com.sparta.deliverypathservice.service.DeliveryPathService;
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

    private final DeliveryPathService deliveryPathService;

    @PostMapping
    public ApiResponse<CreateDeliveryPathResDto> create(
            @RequestBody @Valid CreateDeliveryPathReqDto reqDto
    ) {

        //TODO admin 권한 확인
        CreateDeliveryPathResDto data = deliveryPathService.create(reqDto);
        return new ApiResponse<>(data);
    }

    @GetMapping
    public ApiResponse<Page<getDeliveryPathListResDto>> getDeliveryPaths(
            @RequestBody getDeliveryPathListReqDto reqDto
    ) {
        //TODO 권한 확인 : 어드민, 허브관리자, 허브배송담당자
        Page<getDeliveryPathListResDto> data = deliveryPathService.getDeliveryPaths(reqDto);
        return new ApiResponse<>(data);
    }

    @GetMapping("{id}")
    public ApiResponse<getDeliveryPathResDto> getDeliveryPath(
            @PathVariable UUID id
    ) {
        //TODO 권한 확인 : 어드민, 허브관리자, 업체담당자
        getDeliveryPathResDto data = deliveryPathService.getDeliveryPath(id);
        return new ApiResponse<>(data);
    }

    @PutMapping("{id}")
    public ApiResponse<UpdateDeliveryPathResDto> updateDeliveryPath(
            @PathVariable UUID id,
            @RequestBody UpdateDeliveryPathReqDto reqDto
    ) {
        //TODO admin 권한 확인
        UpdateDeliveryPathResDto data = deliveryPathService.updateDeliveryPath(id, reqDto);
        return new ApiResponse<>(data);
    }

    @DeleteMapping
    public ApiResponse<List<DeleteDeliveryPathResDto>> deleteDeliveryPath(
            @RequestBody List<UUID> paths
    ) {
        //TODO admin 권한 확인
        List<DeleteDeliveryPathResDto> data = deliveryPathService.deleteDeliveryPath(paths);
        return new ApiResponse<>(data);
    }

    @PatchMapping("{id}/state")
    public ApiResponse<UpdateDeliveryPathStateResDto> updateDeliveryPathState(
            @PathVariable UUID id,
            DeliveryPathState state
    ) {
        //TODO 권한 확인 : 어드민, 허브배송담당자
        UpdateDeliveryPathStateResDto data = deliveryPathService.updateDeliveryPathState(id, state);
        return new ApiResponse<>(data);
    }
}
