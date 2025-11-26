package org.skypro.recommendation_service.service.rule;

import org.skypro.recommendation_service.model.dto.DynamicRuleRequest;
import org.skypro.recommendation_service.model.dto.DynamicRuleResponse;
import org.skypro.recommendation_service.model.dto.RecommendationDTO;
import org.skypro.recommendation_service.model.entity.DynamicRule;
import org.skypro.recommendation_service.model.rule.DynamicRuleCondition;
import org.skypro.recommendation_service.repository.DynamicRuleRepository;
import org.skypro.recommendation_service.service.RuleStatisticService;
import org.skypro.recommendation_service.service.cache.RecommendationCacheService;
import org.skypro.recommendation_service.service.rule.command.RuleCommand;
import org.skypro.recommendation_service.service.rule.command.RuleCommandFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final RecommendationCacheService cacheService;
    private final RuleStatisticService ruleStatisticService;
    private final RuleCommandFactory ruleCommandFactory;

    public DynamicRuleService(
            DynamicRuleRepository dynamicRuleRepository,
            RecommendationCacheService cacheService,
            RuleStatisticService ruleStatisticService,
            RuleCommandFactory ruleCommandFactory) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.cacheService = cacheService;
        this.ruleStatisticService = ruleStatisticService;
        this.ruleCommandFactory = ruleCommandFactory;
    }

    public List<RecommendationDTO> checkDynamicRulesForUser(UUID userId) {
        return cacheService.getCachedDynamicRecommendations(userId, () -> {
            List<DynamicRule> activeRules = dynamicRuleRepository.findByActiveTrue();

            return activeRules.stream()
                    .filter(rule -> {
                        boolean matches = evaluateRule(rule.getCondition(), userId);
                        if (matches) {
                            ruleStatisticService.incrementRuleExecution(
                                    rule.getId(), rule.getName()
                            );
                        }
                        return matches;
                    })
                    .map(rule -> new RecommendationDTO(
                            rule.getProductName(),
                            rule.getProductId(),
                            rule.getDescription()
                    ))
                    .collect(Collectors.toList());
        });
    }

    private boolean evaluateRule(DynamicRuleCondition condition, UUID userId) {
        RuleCommand command = ruleCommandFactory.createCommand(condition);
        return command.execute(userId);
    }

    public DynamicRuleResponse createRule(DynamicRuleRequest request) {
        DynamicRule rule = new DynamicRule(
                request.getName(),
                request.getDescription(),
                request.getProductName(),
                request.getProductId(),
                request.getCondition()
        );

        DynamicRule savedRule = dynamicRuleRepository.save(rule);
        cacheService.clearDynamicRulesCache();

        return convertToResponse(savedRule);
    }

    public void deleteRule(UUID ruleId) {
        ruleStatisticService.deleteRuleStatistic(ruleId);
        dynamicRuleRepository.deleteById(ruleId);
        cacheService.clearDynamicRulesCache();
    }

    public List<DynamicRuleResponse> getAllRules() {
        return dynamicRuleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private DynamicRuleResponse convertToResponse(DynamicRule rule) {
        DynamicRuleResponse response = new DynamicRuleResponse();
        response.setId(rule.getId());
        response.setName(rule.getName());
        response.setDescription(rule.getDescription());
        response.setProductName(rule.getProductName());
        response.setProductId(rule.getProductId());
        response.setCondition(rule.getCondition());
        response.setActive(rule.isActive());
        return response;
    }
}
