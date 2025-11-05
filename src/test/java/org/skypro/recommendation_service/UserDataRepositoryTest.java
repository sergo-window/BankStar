package org.skypro.recommendation_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.recommendation_service.repository.UserDataRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDataRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserDataRepository userDataRepository;

    @Test
    void testHasProductType() {
        UUID userId = UUID.randomUUID();
        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), any(), any()))
                .thenReturn(true);

        boolean result = userDataRepository.hasProductType(userId, "DEBIT");

        assertTrue(result);
    }

    @Test
    void testGetTotalDepositsAmount() {
        UUID userId = UUID.randomUUID();
        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any()))
                .thenReturn(new BigDecimal("1500"));

        BigDecimal result = userDataRepository.getTotalDepositsAmount(userId, "SAVING");

        assertEquals(new BigDecimal("1500"), result);
    }
}
