package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.UrlResponse;
import com.haider.LinkFlow.dtos.request.UrlRequest;
import com.haider.LinkFlow.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/url")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping
    public ResponseEntity<?> createUrl(@RequestBody @Valid UrlRequest urlRequest) {
        UrlResponse urlResponse = urlService.addUrl(urlRequest);
        return ResponseEntity.ok(urlResponse);
    }

    @GetMapping("{url}")
    public ResponseEntity<?> getUrl(@PathVariable String url) {
        String longUrl = urlService.getByShortCode(url);
        return ResponseEntity.status(301).location(URI.create(longUrl)).build();
    }
}
