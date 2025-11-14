package org.skypro.recommendation_service.model.dto;

import org.skypro.recommendation_service.model.rule.DynamicRuleCondition;

import java.util.UUID;

public class DynamicRuleResponse {
    private UUID id;
    private String name;
    private String description;
    private String productName;
    private UUID productId;
    private DynamicRuleCondition condition;
    private boolean active;

    public DynamicRuleResponse() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public DynamicRuleCondition getCondition() { return condition; }
    public void setCondition(DynamicRuleCondition condition) { this.condition = condition; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
