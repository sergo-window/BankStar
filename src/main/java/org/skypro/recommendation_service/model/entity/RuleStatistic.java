package org.skypro.recommendation_service.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "rule_statistics")
public class RuleStatistic {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "rule_id", nullable = false, unique = true)
    private UUID ruleId;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Column(name = "execution_count", nullable = false)
    private Long executionCount = 0L;

    public RuleStatistic() {
    }

    public RuleStatistic(UUID ruleId, String ruleName) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Long executionCount) {
        this.executionCount = executionCount;
    }

    public void incrementCount() {
        this.executionCount++;
    }
}
