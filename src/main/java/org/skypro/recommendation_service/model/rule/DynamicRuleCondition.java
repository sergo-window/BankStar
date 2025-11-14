package org.skypro.recommendation_service.model.rule;

import org.skypro.recommendation_service.model.enums.*;

import java.util.List;

public class DynamicRuleCondition {
    private RuleType ruleType;
    private List<String> arguments;

    public DynamicRuleCondition() {}

    public DynamicRuleCondition(RuleType ruleType, List<String> arguments) {
        this.ruleType = ruleType;
        this.arguments = arguments;
    }

    public RuleType getRuleType() { return ruleType; }
    public void setRuleType(RuleType ruleType) { this.ruleType = ruleType; }

    public List<String> getArguments() { return arguments; }
    public void setArguments(List<String> arguments) { this.arguments = arguments; }
}
