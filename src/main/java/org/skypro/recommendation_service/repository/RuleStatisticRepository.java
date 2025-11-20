package org.skypro.recommendation_service.repository;

import org.skypro.recommendation_service.model.entity.RuleStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleStatisticRepository extends JpaRepository<RuleStatistic, UUID> {

    Optional<RuleStatistic> findByRuleId(UUID ruleId);

    @Query("SELECT rs FROM RuleStatistic rs ORDER BY rs.executionCount DESC")
    List<RuleStatistic> findAllOrderByCountDesc();

    @Modifying
    @Query("DELETE FROM RuleStatistic rs WHERE rs.ruleId = :ruleId")
    void deleteByRuleId(UUID ruleId);
}
