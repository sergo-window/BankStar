package org.skypro.recommendation_service.service.rule.command;

import org.skypro.recommendation_service.model.enums.ComparisonOperator;
import org.skypro.recommendation_service.model.enums.OperationType;
import org.skypro.recommendation_service.model.enums.ProductType;
import org.skypro.recommendation_service.repository.UserDataRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TransactionSumCompareRuleCommand implements RuleCommand {

    private final UserDataRepository userDataRepository;
    private final List<String> arguments;

    public TransactionSumCompareRuleCommand(UserDataRepository userDataRepository, List<String> arguments) {
        this.userDataRepository = userDataRepository;
        this.arguments = arguments;
    }

    @Override
    public boolean execute(UUID userId) {
        if (arguments.size() != 4) {
            throw new IllegalArgumentException("TRANSACTION_SUM_COMPARE rule requires exactly 4 arguments");
        }

        ProductType productType = ProductType.valueOf(arguments.get(0));
        OperationType operationType = OperationType.valueOf(arguments.get(1));
        ComparisonOperator operator = ComparisonOperator.fromSymbol(arguments.get(2));
        BigDecimal comparisonValue = new BigDecimal(arguments.get(3));

        BigDecimal totalAmount = userDataRepository.getTotalAmount(
                userId, productType.name(), operationType
        );

        return compareValues(totalAmount, comparisonValue, operator);
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
