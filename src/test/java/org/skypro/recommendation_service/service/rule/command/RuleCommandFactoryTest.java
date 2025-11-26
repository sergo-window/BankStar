package org.skypro.recommendation_service.service.rule.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.recommendation_service.model.enums.RuleType;
import org.skypro.recommendation_service.model.rule.DynamicRuleCondition;
import org.skypro.recommendation_service.repository.UserDataRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RuleCommandFactoryTest {

    @Mock
    private UserDataRepository userDataRepository;

    @InjectMocks
    private RuleCommandFactory ruleCommandFactory;

    @Test
    void testCreateCommand_ForUserOfRule_ShouldReturnUserOfRuleCommand() {

        DynamicRuleCondition condition = new DynamicRuleCondition(RuleType.USER_OF, List.of("DEBIT"));

        RuleCommand command = ruleCommandFactory.createCommand(condition);

        assertInstanceOf(UserOfRuleCommand.class, command);
    }

    @Test
    void testCreateCommand_ForActiveUserOfRule_ShouldReturnActiveUserOfRuleCommand() {

        DynamicRuleCondition condition = new DynamicRuleCondition(RuleType.ACTIVE_USER_OF, List.of("DEBIT"));

        RuleCommand command = ruleCommandFactory.createCommand(condition);

        assertInstanceOf(ActiveUserOfRuleCommand.class, command);
    }
}
