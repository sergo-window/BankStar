package org.skypro.recommendation_service.service;

import org.skypro.recommendation_service.model.entity.RuleStatistic;
import org.skypro.recommendation_service.repository.RuleStatisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RuleStatisticService {

    private final RuleStatisticRepository ruleStatisticRepository;

    public RuleStatisticService(RuleStatisticRepository ruleStatisticRepository) {
        this.ruleStatisticRepository = ruleStatisticRepository;
    }

    @Transactional
    public void incrementRuleExecution(UUID ruleId, String ruleName) {
        RuleStatistic statistic = ruleStatisticRepository.findByRuleId(ruleId)
                .orElse(new RuleStatistic(ruleId, ruleName));

        statistic.incrementCount();
        ruleStatisticRepository.save(statistic);
    }

    @Transactional
    public void deleteRuleStatistic(UUID ruleId) {
        ruleStatisticRepository.deleteByRuleId(ruleId);
    }

    public List<RuleStatistic> getAllStatistics() {
        return ruleStatisticRepository.findAllOrderByCountDesc();
    }
}
