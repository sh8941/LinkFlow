package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.DashboardSummaryResponse;
import com.haider.LinkFlow.dtos.reponse.UrlWrapper;
import com.haider.LinkFlow.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class DashBoardController {

    @Autowired
    UrlService urlService;

    @GetMapping("/api/dashboard/top-urls")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved top URLs")
    @Operation(summary = "Get Top URLs", description = "Retrieve the top URLs based on their usage or popularity.")
    public ResponseEntity<List<UrlWrapper>> getTopUrls() {
        List<UrlWrapper> topUrls = urlService.getTopUrlsActiveUrls();
        return ResponseEntity.ok(topUrls);
    }

    @GetMapping("/api/dashboard/summary")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved dashboard summary")
    @Operation(summary = "Get Dashboard Summary", description = "Retrieve the dashboard summary for a specific period.")
    public ResponseEntity<DashboardSummaryResponse> getSummaryOfPeriod(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        DashboardSummaryResponse response = urlService.getSummary(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dashboard/summary-overall")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved overall dashboard summary")
    @Operation(summary = "Get Overall Dashboard Summary", description = "Retrieve the overall dashboard summary.")
    public ResponseEntity<DashboardSummaryResponse> getSummaryOverAll() {
        DashboardSummaryResponse response = urlService.getSummaryOverAll();
        return ResponseEntity.ok(response);
    }
}
