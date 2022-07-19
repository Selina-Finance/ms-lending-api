package com.selina.lending.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application")
@Slf4j
public class LendingController {

    @GetMapping(value = "/dip")
    public ResponseEntity get() {
        log.info("LendingController get()");
        return ResponseEntity.ok().body("Hello World");
    }
}
