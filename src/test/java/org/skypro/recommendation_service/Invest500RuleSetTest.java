package org.skypro.recommendation_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.recommendation_service.repository.UserDataRepository;
import org.skypro.recommendation_service.service.rule.impl.Invest500RuleSet;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Invest500RuleSetTest {

    @Mock
    private UserDataRepository userDataRepository;

    @InjectMocks
    private Invest500RuleSet invest500RuleSet;

    @Test
    void testCheckUser_AllRulesPass() {
        UUID userId = UUID.randomUUID();

        when(userDataRepository.hasProductType(userId, "DEBIT")).thenReturn(true);
        when(userDataRepository.hasProductType(userId, "INVEST")).thenReturn(false);
        when(userDataRepository.getTotalDepositsAmount(userId, "SAVING"))
                .thenReturn(new BigDecimal("1500"));

        Optional<?> result = invest500RuleSet.checkUser(userId);

        assertTrue(result.isPresent());
    }
}
