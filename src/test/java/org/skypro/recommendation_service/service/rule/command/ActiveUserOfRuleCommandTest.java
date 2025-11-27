package org.skypro.recommendation_service.service.rule.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.recommendation_service.repository.UserDataRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActiveUserOfRuleCommandTest {

    @Mock
    private UserDataRepository userDataRepository;

    private final UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

    @Test
    void testExecute_WhenTransactionCountMeetsThreshold_ShouldReturnTrue() {

        when(userDataRepository.getTransactionCount(any(UUID.class), eq("DEBIT")))
                .thenReturn(10);

        ActiveUserOfRuleCommand command = new ActiveUserOfRuleCommand(userDataRepository, List.of("DEBIT"));

        boolean result = command.execute(userId);

        assertTrue(result);
    }

    @Test
    void testExecute_WhenTransactionCountBelowThreshold_ShouldReturnFalse() {

        when(userDataRepository.getTransactionCount(any(UUID.class), eq("DEBIT")))
                .thenReturn(3);

        ActiveUserOfRuleCommand command = new ActiveUserOfRuleCommand(userDataRepository, List.of("DEBIT"));

        boolean result = command.execute(userId);

        assertFalse(result);
    }
}
