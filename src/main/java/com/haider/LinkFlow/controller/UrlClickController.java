package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.UrlClickResponse;
import com.haider.LinkFlow.service.UrlClickService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clicks")
@CrossOrigin(origins = "http://localhost:5173")
public class UrlClickController {
    @Autowired
    private UrlClickService urlClickService;

    @GetMapping("/{url}")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved clicks for the URL")
    @Operation(summary = "Get Clicks for URL", description = "Retrieve the clicks for a specific URL with pagination support.")
    public ResponseEntity<List<UrlClickResponse>> getClicks(@PathVariable String url,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("clickedAt").descending());
        List<UrlClickResponse> clickResponses = urlClickService.getUrlClicks(url,pageable);
        return ResponseEntity.ok(clickResponses);
    }

    @GetMapping("/my-url-clicks")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved clicks for the user's URLs")
    @Operation(summary = "Get My URL Clicks", description = "Retrieve the clicks for the user's URLs within a specific date range.")
    public ResponseEntity<List<UrlClickResponse>> getMyUrlClicks(@RequestParam LocalDate startDate,
                                                                @RequestParam LocalDate endDate) {
        List<UrlClickResponse> clicks = urlClickService.getMyUrlClicks(startDate, endDate);
        return ResponseEntity.ok(clicks);
    }
}
