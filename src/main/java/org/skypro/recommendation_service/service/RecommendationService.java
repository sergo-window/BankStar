package org.skypro.recommendation_service.service;

import org.skypro.recommendation_service.model.dto.RecommendationDTO;
import org.skypro.recommendation_service.model.dto.RecommendationResponse;
import org.skypro.recommendation_service.service.rule.DynamicRuleService;
import org.skypro.recommendation_service.service.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final DynamicRuleService dynamicRuleService;

    public RecommendationService(
            List<RecommendationRuleSet> ruleSets,
            DynamicRuleService dynamicRuleService) {
        this.ruleSets = ruleSets;
        this.dynamicRuleService = dynamicRuleService;
    }

    public RecommendationResponse getRecommendationsForUser(UUID userId) {

        List<RecommendationDTO> recommendations = new ArrayList<>();

        List<RecommendationDTO> fixedRecommendations = ruleSets.stream()
                .map(ruleSet -> ruleSet.checkUser(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        recommendations.addAll(fixedRecommendations);

        List<RecommendationDTO> dynamicRecommendations =
                dynamicRuleService.checkDynamicRulesForUser(userId);
        recommendations.addAll(dynamicRecommendations);

        return new RecommendationResponse(userId, recommendations);
    }
}
