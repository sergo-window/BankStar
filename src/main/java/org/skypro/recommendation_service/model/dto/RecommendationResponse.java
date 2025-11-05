package org.skypro.recommendation_service.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class RecommendationResponse {

    @JsonProperty("user_id")
    private UUID userId;

    private List<RecommendationDTO> recommendations;

    /**
     * Конструктор по умолчанию нужен для правильной работы Spring/Jackson (создаёт пустой объект)
     */
    public RecommendationResponse() {
    }

    /**
     * Параметризованный конструктор для бизнес-логики и тестирования
     */
    public RecommendationResponse(UUID userId, List<RecommendationDTO> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<RecommendationDTO> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendationDTO> recommendations) {
        this.recommendations = recommendations;
    }
}