package org.skypro.recommendation_service.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.recommendation_service.model.enums.OperationType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    void testGetTotalAmountByOperationType_Deposit() {
        UUID userId = UUID.randomUUID();
        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), any()))
                .thenReturn(new BigDecimal("1500"));

        BigDecimal result = userDataRepository.getTotalAmount(
                userId, "SAVING", OperationType.DEPOSIT
        );

        assertEquals(new BigDecimal("1500"), result);

        verify(jdbcTemplate).queryForObject(
                anyString(),
                eq(BigDecimal.class),
                eq(userId),
                eq("SAVING"),
                eq("DEPOSIT")
        );
    }

    @Test
    void testGetTotalAmountByOperationType_Spend() {
        UUID userId = UUID.randomUUID();
        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), any()))
                .thenReturn(new BigDecimal("800"));

        BigDecimal result = userDataRepository.getTotalAmount(
                userId, "DEBIT", OperationType.SPEND
        );

        assertEquals(new BigDecimal("800"), result);

        verify(jdbcTemplate).queryForObject(
                anyString(),
                eq(BigDecimal.class),
                eq(userId),
                eq("DEBIT"),
                eq("SPEND")
        );
    }

    @Test
    void testGetTotalAmountByOperationType_ReturnsZeroWhenNull() {
        UUID userId = UUID.randomUUID();
        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), any()))
                .thenReturn(null);

        BigDecimal result = userDataRepository.getTotalAmount(
                userId, "CREDIT", OperationType.DEPOSIT
        );

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testGetTotalDepositsAmount() {
        UUID userId = UUID.randomUUID();

        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), any()))
                .thenReturn(new BigDecimal("1500"));

        BigDecimal result = userDataRepository.getTotalDepositsAmount(userId, "SAVING");

        assertEquals(new BigDecimal("1500"), result);

        verify(jdbcTemplate).queryForObject(
                anyString(),
                eq(BigDecimal.class),
                eq(userId),
                eq("SAVING"),
                eq("DEPOSIT")
        );
    }

    @Test
    void testGetTotalSpendsAmount() {
        UUID userId = UUID.randomUUID();
        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), any()))
                .thenReturn(new BigDecimal("500"));

        BigDecimal result = userDataRepository.getTotalSpendsAmount(userId, "DEBIT");

        assertEquals(new BigDecimal("500"), result);

        verify(jdbcTemplate).queryForObject(
                anyString(),
                eq(BigDecimal.class),
                eq(userId),
                eq("DEBIT"),
                eq("SPEND")
        );
    }

    @Test
    void testIsDepositsGreaterThanSpends_WhenDepositsGreater() {
        UUID userId = UUID.randomUUID();

        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), eq("DEPOSIT")))
                .thenReturn(new BigDecimal("1000"));
        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), eq("SPEND")))
                .thenReturn(new BigDecimal("500"));

        boolean result = userDataRepository.isDepositsGreaterThanSpends(userId, "DEBIT");

        assertTrue(result);
    }

    @Test
    void testIsDepositsGreaterThanSpends_WhenDepositsLess() {
        UUID userId = UUID.randomUUID();

        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), eq("DEPOSIT")))
                .thenReturn(new BigDecimal("300"));
        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), eq("SPEND")))
                .thenReturn(new BigDecimal("800"));

        boolean result = userDataRepository.isDepositsGreaterThanSpends(userId, "DEBIT");

        assertFalse(result);
    }

    @Test
    void testIsDepositsGreaterThanSpends_WhenEqual() {
        UUID userId = UUID.randomUUID();

        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), eq("DEPOSIT")))
                .thenReturn(new BigDecimal("500"));
        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), any(), any(), eq("SPEND")))
                .thenReturn(new BigDecimal("500"));

        boolean result = userDataRepository.isDepositsGreaterThanSpends(userId, "DEBIT");

        assertFalse(result);
    }
}
