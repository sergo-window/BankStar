package org.skypro.recommendation_service.service.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.skypro.recommendation_service.model.dto.RecommendationDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class RecommendationCacheService {

    private final Cache<UUID, List<RecommendationDTO>> dynamicRecommendationsCache;
    private final Cache<String, List<RecommendationDTO>> fixedRecommendationsCache;

    public RecommendationCacheService() {
        this.dynamicRecommendationsCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        this.fixedRecommendationsCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    public List<RecommendationDTO> getCachedDynamicRecommendations(UUID userId, Supplier<List<RecommendationDTO>> recommendationsSupplier) {
        return dynamicRecommendationsCache.get(userId, key -> recommendationsSupplier.get());
    }

    public List<RecommendationDTO> getCachedFixedRecommendations(String cacheKey, Supplier<List<RecommendationDTO>> recommendationsSupplier) {
        return fixedRecommendationsCache.get(cacheKey, key -> recommendationsSupplier.get());
    }

    public void clearDynamicRulesCache() {
        dynamicRecommendationsCache.invalidateAll();
    }

    public void clearUserCache(UUID userId) {
        dynamicRecommendationsCache.invalidate(userId);
    }
}
