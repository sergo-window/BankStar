package org.skypro.recommendation_service.service.rule.command;

import org.skypro.recommendation_service.model.enums.ProductType;
import org.skypro.recommendation_service.repository.UserDataRepository;

import java.util.List;
import java.util.UUID;

public class ActiveUserOfRuleCommand implements RuleCommand {

    private final UserDataRepository userDataRepository;
    private final List<String> arguments;

    public ActiveUserOfRuleCommand(UserDataRepository userDataRepository, List<String> arguments) {
        this.userDataRepository = userDataRepository;
        this.arguments = arguments;
    }

    @Override
    public boolean execute(UUID userId) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("ACTIVE_USER_OF rule requires exactly 1 argument");
        }
        ProductType productType = ProductType.valueOf(arguments.get(0));
        int transactionCount = userDataRepository.getTransactionCount(userId, productType.name());
        return transactionCount >= 5;
    }
}
