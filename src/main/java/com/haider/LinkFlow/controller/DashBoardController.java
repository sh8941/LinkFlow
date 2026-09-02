package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.DashboardSummaryResponse;
import com.haider.LinkFlow.dtos.reponse.UrlWrapper;
import com.haider.LinkFlow.service.UrlService;
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
    public ResponseEntity<?> getTopUrls() {
        List<UrlWrapper> topUrls = urlService.getTopUrlsActiveUrls();
        return ResponseEntity.ok(topUrls);
    }

    @GetMapping("/api/dashboard/summary")
    public ResponseEntity<?> getSummaryOfPeriod(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        DashboardSummaryResponse response = urlService.getSummary(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dashboard/summary-overall")
    public ResponseEntity<?> getSummaryOverAll() {
        DashboardSummaryResponse response = urlService.getSummaryOverAll();
        return ResponseEntity.ok(response);
    }
}
