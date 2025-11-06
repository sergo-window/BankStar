package org.skypro.recommendation_service.model.dto;

import java.util.UUID;

public class RecommendationDTO {
    private String name;
    private UUID id;
    private String text;

    /**
     * Конструктор по умолчанию нужен для правильной работы Spring/Jackson (создаёт пустой объект)
     */
    public RecommendationDTO() {
    }

    /**
     * Параметризованный конструктор для бизнес-логики и тестирования
     */
    public RecommendationDTO(String name, UUID id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
