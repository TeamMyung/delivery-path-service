package com.sparta.deliverypathservice.dto.request;

import com.sparta.deliverypathservice.dto.Pageable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDeliveryPathListReqDto {

    private Pageable pageable;
    private String search;
}
