package org.skypro.recommendation_service.service.rule.command;

import org.skypro.recommendation_service.model.enums.ProductType;
import org.skypro.recommendation_service.repository.UserDataRepository;

import java.util.List;
import java.util.UUID;

public class UserOfRuleCommand implements RuleCommand {

    private final UserDataRepository userDataRepository;
    private final List<String> arguments;

    public UserOfRuleCommand(UserDataRepository userDataRepository, List<String> arguments) {
        this.userDataRepository = userDataRepository;
        this.arguments = arguments;
    }

    @Override
    public boolean execute(UUID userId) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("USER_OF rule requires exactly 1 argument");
        }
        ProductType productType = ProductType.valueOf(arguments.get(0));
        return userDataRepository.hasProductType(userId, productType.name());
    }
}
