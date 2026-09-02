package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.DashboardSummaryResponse;
import com.haider.LinkFlow.dtos.reponse.PageResponse;
import com.haider.LinkFlow.dtos.reponse.UrlResponse;
import com.haider.LinkFlow.dtos.reponse.UrlWrapper;
import com.haider.LinkFlow.dtos.request.UrlRequest;
import com.haider.LinkFlow.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/api/url")
    public ResponseEntity<?> createUrl(@RequestBody @Valid UrlRequest urlRequest) {
        UrlResponse urlResponse = urlService.addUrl(urlRequest);
        return ResponseEntity.ok(urlResponse.getShortCode());
    }

    @GetMapping("/api/url/my")
    ResponseEntity<?> getMyUrls(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("clickCount").descending());
        PageResponse urls = urlService.getMyUrls(pageable);
        return ResponseEntity.ok(urls);
    }

    @DeleteMapping("/api/url/{url}")
    public ResponseEntity<?> deleteUrl(@PathVariable String url) throws Exception {
        urlService.deactivateUrl(url);
        return ResponseEntity.noContent().build();
    }

}
