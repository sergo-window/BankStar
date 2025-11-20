package org.skypro.recommendation_service.controller;

import org.skypro.recommendation_service.model.dto.RuleStatisticDTO;
import org.skypro.recommendation_service.model.dto.RuleStatisticResponse;
import org.skypro.recommendation_service.model.entity.RuleStatistic;
import org.skypro.recommendation_service.service.RuleStatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleStatisticController {

    private final RuleStatisticService ruleStatisticService;

    public RuleStatisticController(RuleStatisticService ruleStatisticService) {
        this.ruleStatisticService = ruleStatisticService;
    }

    @GetMapping("/stats")
    public ResponseEntity<RuleStatisticResponse> getRuleStats() {
        List<RuleStatistic> statistics = ruleStatisticService.getAllStatistics();

        List<RuleStatisticDTO> statsDTO = statistics.stream()
                .map(stat -> new RuleStatisticDTO(stat.getRuleId(), stat.getExecutionCount()))
                .collect(Collectors.toList());


        RuleStatisticResponse response = new RuleStatisticResponse(statsDTO);
        return ResponseEntity.ok(response);
    }
}
