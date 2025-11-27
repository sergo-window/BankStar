package org.skypro.recommendation_service.service.rule.command;

import org.skypro.recommendation_service.model.enums.ComparisonOperator;
import org.skypro.recommendation_service.model.enums.OperationType;
import org.skypro.recommendation_service.model.enums.ProductType;
import org.skypro.recommendation_service.repository.UserDataRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TransactionSumCompareDepositWithdrawRuleCommand implements RuleCommand {

    private final UserDataRepository userDataRepository;
    private final List<String> arguments;

    public TransactionSumCompareDepositWithdrawRuleCommand(UserDataRepository userDataRepository, List<String> arguments) {
        this.userDataRepository = userDataRepository;
        this.arguments = arguments;
    }

    @Override
    public boolean execute(UUID userId) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW rule requires exactly 2 arguments");
        }

        ProductType productType = ProductType.valueOf(arguments.get(0));
        ComparisonOperator operator = ComparisonOperator.fromSymbol(arguments.get(1));

        BigDecimal deposits = userDataRepository.getTotalAmount(
                userId, productType.name(), OperationType.DEPOSIT
        );
        BigDecimal spends = userDataRepository.getTotalAmount(
                userId, productType.name(), OperationType.SPEND
        );

        return compareValues(deposits, spends, operator);
    }

    private boolean compareValues(BigDecimal value1, BigDecimal value2, ComparisonOperator operator) {
        return switch (operator) {
            case GREATER_THAN -> value1.compareTo(value2) > 0;
            case LESS_THAN -> value1.compareTo(value2) < 0;
            case EQUALS -> value1.compareTo(value2) == 0;
            case GREATER_THAN_OR_EQUALS -> value1.compareTo(value2) >= 0;
            case LESS_THAN_OR_EQUALS -> value1.compareTo(value2) <= 0;
        };
    }
}
