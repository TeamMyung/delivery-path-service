package com.sparta.deliverypathservice.dto;

import lombok.Getter;

@Getter
public class Pageable {

    private int page;
    private int size;
    private String sortBy;
    private boolean isAsc;
}
