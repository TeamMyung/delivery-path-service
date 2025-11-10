package com.sparta.deliverypathservice.controller;

import com.sparta.deliverypathservice.domain.DeliveryPathState;
import com.sparta.deliverypathservice.dto.request.CreateDeliveryPathReqDto;
import com.sparta.deliverypathservice.dto.response.CreateDeliveryPathResDto;
import com.sparta.deliverypathservice.dto.response.DeleteDeliveryPathResDto;
import com.sparta.deliverypathservice.dto.response.UpdateDeliveryPathStateResDto;
import com.sparta.deliverypathservice.global.config.ApiResponse;
import com.sparta.deliverypathservice.service.DeliveryPathService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/delivery-paths/api")
public class DeliveryPathApiController {

    private final DeliveryPathService deliveryPathService;

    @PostMapping
    public ApiResponse<CreateDeliveryPathResDto> create(
            @RequestBody @Valid CreateDeliveryPathReqDto reqDto
    ) {
        CreateDeliveryPathResDto data = deliveryPathService.create(reqDto);
        return new ApiResponse<>(data);
    }

    @DeleteMapping
    public ApiResponse<List<DeleteDeliveryPathResDto>> deleteDeliveryPath(
            @RequestBody List<UUID> paths
    ) {
        List<DeleteDeliveryPathResDto> data = deliveryPathService.deleteDeliveryPath(paths);
        return new ApiResponse<>(data);
    }

    @PatchMapping("{id}/state")
    public ApiResponse<UpdateDeliveryPathStateResDto> updateDeliveryPathState(
            @PathVariable UUID id,
            DeliveryPathState state
    ) {
        UpdateDeliveryPathStateResDto data = deliveryPathService.updateDeliveryPathState(id, state);
        return new ApiResponse<>(data);
    }
}
