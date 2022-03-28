package com.project.roomescape.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    // Nginx health check
    @GetMapping("/health")
    public String checkHealth() {
        return "healthy"; }


}
