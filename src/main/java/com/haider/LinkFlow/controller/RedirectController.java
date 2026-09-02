package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.service.UrlClickService;
import com.haider.LinkFlow.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RedirectController {
    @Autowired
    private UrlService urlService;
    @Autowired
    private UrlClickService urlClickService;

    @GetMapping("/go/{url}")
    public ResponseEntity<?> getUrl(@PathVariable String url, HttpServletRequest request) {
        String longUrl = urlService.getByShortCode(url);
        urlClickService.trackClick(request,url);
        return ResponseEntity.status(302).location(URI.create(longUrl)).build();
    }
}
