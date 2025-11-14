package org.skypro.recommendation_service.repository;

import org.skypro.recommendation_service.model.entity.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {

    List<DynamicRule> findByActiveTrue();

    @Query("SELECT dr FROM DynamicRule dr WHERE dr.active = true ORDER BY dr.name")
    List<DynamicRule> findAllActiveRules();
}
