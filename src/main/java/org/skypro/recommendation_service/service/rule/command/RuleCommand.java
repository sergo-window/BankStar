package org.skypro.recommendation_service.service.rule.command;

import java.util.UUID;

public interface RuleCommand {
    boolean execute(UUID userId);
}
