package org.skypro.recommendation_service.model.dto;

import java.util.List;

public class RuleStatisticResponse {
    private List<RuleStatisticDTO> stats;

    public RuleStatisticResponse() {}

    public RuleStatisticResponse(List<RuleStatisticDTO> stats) {
        this.stats = stats;
    }

    public List<RuleStatisticDTO> getStats() { return stats; }
    public void setStats(List<RuleStatisticDTO> stats) { this.stats = stats; }
}
