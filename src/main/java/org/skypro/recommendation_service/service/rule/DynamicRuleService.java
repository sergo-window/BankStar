package org.skypro.recommendation_service.service.rule;

import org.skypro.recommendation_service.model.dto.DynamicRuleRequest;
import org.skypro.recommendation_service.model.dto.DynamicRuleResponse;
import org.skypro.recommendation_service.model.dto.RecommendationDTO;
import org.skypro.recommendation_service.model.entity.DynamicRule;
import org.skypro.recommendation_service.model.enums.*;
import org.skypro.recommendation_service.model.rule.DynamicRuleCondition;
import org.skypro.recommendation_service.repository.DynamicRuleRepository;
import org.skypro.recommendation_service.repository.UserDataRepository;
import org.skypro.recommendation_service.service.RuleStatisticService;
import org.skypro.recommendation_service.service.cache.RecommendationCacheService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final UserDataRepository userDataRepository;
    private final RecommendationCacheService cacheService;
    private final RuleStatisticService ruleStatisticService;

    public DynamicRuleService(
            DynamicRuleRepository dynamicRuleRepository,
            UserDataRepository userDataRepository,
            RecommendationCacheService cacheService,
            RuleStatisticService ruleStatisticService) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.userDataRepository = userDataRepository;
        this.cacheService = cacheService;
        this.ruleStatisticService = ruleStatisticService;
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
        return switch (condition.getRuleType()) {
            case USER_OF -> evaluateUserOfRule(condition.getArguments(), userId);
            case ACTIVE_USER_OF -> evaluateActiveUserOfRule(condition.getArguments(), userId);
            case TRANSACTION_SUM_COMPARE -> evaluateTransactionSumCompareRule(condition.getArguments(), userId);
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW ->
                    evaluateTransactionSumCompareDepositWithdrawRule(condition.getArguments(), userId);
        };
    }

    private boolean evaluateUserOfRule(List<String> arguments, UUID userId) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("USER_OF rule requires exactly 1 argument");
        }
        ProductType productType = ProductType.valueOf(arguments.get(0));
        return userDataRepository.hasProductType(userId, productType.name());
    }

    private boolean evaluateActiveUserOfRule(List<String> arguments, UUID userId) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("ACTIVE_USER_OF rule requires exactly 1 argument");
        }
        ProductType productType = ProductType.valueOf(arguments.get(0));

        int transactionCount = userDataRepository.getTransactionCount(userId, productType.name());
        return transactionCount >= 5;
    }

    private boolean evaluateTransactionSumCompareRule(List<String> arguments, UUID userId) {
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

    private boolean evaluateTransactionSumCompareDepositWithdrawRule(List<String> arguments, UUID userId) {
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
