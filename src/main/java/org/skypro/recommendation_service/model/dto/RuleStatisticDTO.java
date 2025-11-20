package org.skypro.recommendation_service.model.dto;

import java.util.UUID;

public class RuleStatisticDTO {
    private UUID ruleId;
    private Long count;

    public RuleStatisticDTO() {
    }

    public RuleStatisticDTO(UUID ruleId, Long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
