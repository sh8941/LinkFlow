package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.UrlClickResponse;
import com.haider.LinkFlow.service.UrlClickService;
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
    public ResponseEntity<?> getClicks(@PathVariable String url,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("clickedAt").descending());
        List<UrlClickResponse> clickResponses = urlClickService.getUrlClicks(url,pageable);
        return ResponseEntity.ok(clickResponses);
    }

    @GetMapping("/my-url-clicks")
    public ResponseEntity<?> getMyUrlClicks(@RequestParam LocalDate startDate,
                                           @RequestParam LocalDate endDate) {
        List<UrlClickResponse> clicks = urlClickService.getMyUrlClicks(startDate, endDate);
        return ResponseEntity.ok(clicks);
    }
}
