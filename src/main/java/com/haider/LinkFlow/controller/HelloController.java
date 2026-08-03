package com.haider.LinkFlow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/public")
    public ResponseEntity<String> helloPublic() {
        return ResponseEntity.ok("Hello World... public api..");
    }

    @GetMapping("/private")
    public ResponseEntity<String> helloPrivate() {
        return ResponseEntity.ok("Hello World... private api..");
    }
}
