package org.skypro.recommendation_service.service.rule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.recommendation_service.model.dto.RecommendationDTO;
import org.skypro.recommendation_service.model.entity.DynamicRule;
import org.skypro.recommendation_service.model.enums.RuleType;
import org.skypro.recommendation_service.model.rule.DynamicRuleCondition;
import org.skypro.recommendation_service.repository.DynamicRuleRepository;
import org.skypro.recommendation_service.service.RuleStatisticService;
import org.skypro.recommendation_service.service.cache.RecommendationCacheService;
import org.skypro.recommendation_service.service.rule.command.RuleCommand;
import org.skypro.recommendation_service.service.rule.command.RuleCommandFactory;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DynamicRuleServiceTest {

    @Mock
    private DynamicRuleRepository dynamicRuleRepository;

    @Mock
    private RecommendationCacheService cacheService;

    @Mock
    private RuleStatisticService ruleStatisticService;

    @Mock
    private RuleCommandFactory ruleCommandFactory;

    @Mock
    private RuleCommand ruleCommand;

    @InjectMocks
    private DynamicRuleService dynamicRuleService;

    private final UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

    @Test
    void testCheckDynamicRulesForUser_WhenRuleMatches_ShouldReturnRecommendation() {

        DynamicRuleCondition condition = new DynamicRuleCondition(RuleType.USER_OF, List.of("DEBIT"));
        DynamicRule rule = new DynamicRule("Test Rule", "Description", "Product", UUID.randomUUID(), condition);

        when(dynamicRuleRepository.findByActiveTrue()).thenReturn(List.of(rule));
        when(ruleCommandFactory.createCommand(condition)).thenReturn(ruleCommand);
        when(ruleCommand.execute(userId)).thenReturn(true);
        when(cacheService.getCachedDynamicRecommendations(any(), any())).thenAnswer(invocation -> {
            var supplier = (java.util.function.Supplier<List<RecommendationDTO>>) invocation.getArgument(1);
            return supplier.get();
        });

        List<RecommendationDTO> recommendations = dynamicRuleService.checkDynamicRulesForUser(userId);

        assertEquals(1, recommendations.size());
        verify(ruleStatisticService).incrementRuleExecution(rule.getId(), rule.getName());
    }
}
