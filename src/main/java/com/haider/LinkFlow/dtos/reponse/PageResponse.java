package com.haider.LinkFlow.dtos.reponse;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse {
    private List<?> content;
    private long pageNumber;
    private long pageSize;
    private long totalElements;
    private long totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
