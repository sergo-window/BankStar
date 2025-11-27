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
class UserOfRuleCommandTest {

    @Mock
    private UserDataRepository userDataRepository;

    private final UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

    @Test
    void testExecute_WhenUserHasProductType_ShouldReturnTrue() {

        when(userDataRepository.hasProductType(any(UUID.class), eq("DEBIT")))
                .thenReturn(true);

        UserOfRuleCommand command = new UserOfRuleCommand(userDataRepository, List.of("DEBIT"));

        boolean result = command.execute(userId);

        assertTrue(result);
    }

    @Test
    void testExecute_WhenUserDoesNotHaveProductType_ShouldReturnFalse() {

        when(userDataRepository.hasProductType(any(UUID.class), eq("DEBIT")))
                .thenReturn(false);

        UserOfRuleCommand command = new UserOfRuleCommand(userDataRepository, List.of("DEBIT"));

        boolean result = command.execute(userId);

        assertFalse(result);
    }

    @Test
    void testExecute_WhenInvalidArgumentsCount_ShouldThrowException() {

        UserOfRuleCommand command = new UserOfRuleCommand(userDataRepository, List.of("DEBIT", "EXTRA_ARG"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> command.execute(userId)
        );

        assertEquals("USER_OF rule requires exactly 1 argument", exception.getMessage());
    }

    @Test
    void testExecute_WhenInvalidProductType_ShouldThrowException() {

        UserOfRuleCommand command = new UserOfRuleCommand(userDataRepository, List.of("INVALID_TYPE"));

        assertThrows(IllegalArgumentException.class, () -> command.execute(userId));
    }
}
