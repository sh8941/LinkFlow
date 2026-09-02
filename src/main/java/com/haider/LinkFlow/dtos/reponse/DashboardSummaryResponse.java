package com.haider.LinkFlow.dtos.reponse;

import lombok.Data;

@Data
public class DashboardSummaryResponse {
    private Long totalLinks;
    private Long totalClicks;
    private Long uniqueClicks;
    private Float averageClicks;
}
