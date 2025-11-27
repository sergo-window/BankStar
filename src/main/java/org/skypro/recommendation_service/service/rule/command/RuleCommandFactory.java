package org.skypro.recommendation_service.service.rule.command;

import org.skypro.recommendation_service.model.enums.RuleType;
import org.skypro.recommendation_service.model.rule.DynamicRuleCondition;
import org.skypro.recommendation_service.repository.UserDataRepository;
import org.springframework.stereotype.Component;

@Component
public class RuleCommandFactory {

    private final UserDataRepository userDataRepository;

    public RuleCommandFactory(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public RuleCommand createCommand(DynamicRuleCondition condition) {
        RuleType ruleType = condition.getRuleType();
        return switch (ruleType) {
            case USER_OF -> new UserOfRuleCommand(userDataRepository, condition.getArguments());
            case ACTIVE_USER_OF -> new ActiveUserOfRuleCommand(userDataRepository, condition.getArguments());
            case TRANSACTION_SUM_COMPARE -> new TransactionSumCompareRuleCommand(userDataRepository, condition.getArguments());
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW ->
                    new TransactionSumCompareDepositWithdrawRuleCommand(userDataRepository, condition.getArguments());
        };
    }
}
