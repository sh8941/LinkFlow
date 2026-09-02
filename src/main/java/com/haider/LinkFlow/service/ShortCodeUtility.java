package com.haider.LinkFlow.service;

import com.haider.LinkFlow.repo.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShortCodeUtility {
    @Autowired
    private UrlRepo urlRepo;

    private String generateShortCode() {

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);
    }

    public String generateUniqueCode() {

        String code;

        do {
            code = generateShortCode();
        } while (urlRepo.existsByShortCode(code));

        return code;
    }

    public String normalize(String url) {
        url = url.trim();

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }
}
