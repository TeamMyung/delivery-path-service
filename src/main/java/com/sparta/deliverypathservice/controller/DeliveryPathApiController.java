package com.sparta.deliverypathservice.controller;

import com.sparta.deliverypathservice.domain.DeliveryPathState;
import com.sparta.deliverypathservice.dto.request.CreateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.response.CreateDeliveryPathResDto;
import com.sparta.deliverypathservice.dto.response.DeleteDeliveryPathResDto;
import com.sparta.deliverypathservice.dto.response.UpdateDeliveryPathStateResDto;
import com.sparta.deliverypathservice.global.dto.ApiResponse;
import com.sparta.deliverypathservice.service.DeliveryPathService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/delivery-paths/api")
public class DeliveryPathApiController {

    private final DeliveryPathService deliveryPathService;

    @Operation(summary = "배송 경로 자동 생성", description = "주문이 생성되면 배송 경로를 생성하는 API 입니다.")
    @PostMapping
    public ApiResponse<CreateDeliveryPathResDto> create(
            @RequestBody @Valid CreateDeliveryPathReqDto reqDto
    ) {
        CreateDeliveryPathResDto data = deliveryPathService.create(reqDto);
        return new ApiResponse<>(data);
    }

    @Operation(summary = "배송 경로 자동 삭제", description = "주문 또는 배송이 삭제되면 배송 경로를 삭제하는 API 입니다.")
    @DeleteMapping
    public ApiResponse<List<DeleteDeliveryPathResDto>> deleteDeliveryPath(
            @RequestBody List<UUID> paths,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        List<DeleteDeliveryPathResDto> data = deliveryPathService.deleteDeliveryPath(paths, token);
        return new ApiResponse<>(data);
    }

    @Operation(summary = "배송 경로 상태 자동 변경", description = "배송 상태가 변경되면 배송 경로 상태를 변경하는 API 입니다.")
    @PatchMapping("{id}/state")
    public ApiResponse<UpdateDeliveryPathStateResDto> updateDeliveryPathState(
            @PathVariable UUID id,
            @RequestParam DeliveryPathState state
    ) {
        UpdateDeliveryPathStateResDto data = deliveryPathService.updateDeliveryPathState(id, state);
        return new ApiResponse<>(data);
    }
}
