package com.reservationsystem.reservation_backend.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipleName = authentication.getName();
        return  ResponseEntity.ok("Hello"+ currentPrincipleName + "!");
    }
}
