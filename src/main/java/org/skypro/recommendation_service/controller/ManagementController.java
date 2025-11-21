package org.skypro.recommendation_service.controller;

import org.skypro.recommendation_service.service.cache.RecommendationCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final RecommendationCacheService cacheService;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.name:recommendation-service}")
    private String appName;

    public ManagementController(RecommendationCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        cacheService.clearDynamicRulesCache();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getServiceInfo() {
        return ResponseEntity.ok(Map.of(
                "name", appName,
                "version", appVersion
        ));
    }
}
