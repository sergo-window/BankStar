package org.skypro.recommendation_service.model.dto;

import org.skypro.recommendation_service.model.rule.DynamicRuleCondition;

import java.util.UUID;

public class DynamicRuleRequest {
    private String name;
    private String description;
    private String productName;
    private UUID productId;
    private DynamicRuleCondition condition;

    public DynamicRuleRequest() {}

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
}
