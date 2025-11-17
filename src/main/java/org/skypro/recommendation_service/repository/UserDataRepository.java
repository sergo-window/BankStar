package org.skypro.recommendation_service.repository;

import org.skypro.recommendation_service.model.enums.OperationType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class UserDataRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserDataRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Проверяем, использует ли пользователь продукты указанного типа
     */
    public boolean hasProductType(UUID userId, String productType) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM user_operations uo
                JOIN bank_products bp ON uo.product_id = bp.id
                WHERE uo.user_id = ? AND bp.type = ?
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType));
    }

    /**
     * Получаем количество транзакций по продуктам указанного типа
     */
    public int getTransactionCount(UUID userId, String productType) {
        String sql = """
                SELECT COUNT(*)
                FROM user_operations uo
                JOIN bank_products bp ON uo.product_id = bp.id
                WHERE uo.user_id = ? AND bp.type = ?
                """;
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
        return result != null ? result : 0;
    }

    /**
     * Получаем общую сумму операций по продуктам указанного типа и типа операции
     */
    public BigDecimal getTotalAmount(UUID userId, String productType, OperationType operationType) {
        String sql = """
                SELECT COALESCE(SUM(uo.amount), 0)
                FROM user_operations uo
                JOIN bank_products bp ON uo.product_id = bp.id
                WHERE uo.user_id = ? AND bp.type = ? AND uo.operation_type = ?
                """;
        BigDecimal result = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, userId, productType, operationType.name()
        );
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * Методы для обратной совместимости
     */
    public BigDecimal getTotalDepositsAmount(UUID userId, String productType) {
        return getTotalAmount(userId, productType, OperationType.DEPOSIT);
    }

    public BigDecimal getTotalSpendsAmount(UUID userId, String productType) {
        return getTotalAmount(userId, productType, OperationType.SPEND);
    }

    /**
     * Проверяем, больше ли сумма пополнений суммы трат для указанного типа продукта
     */
    public boolean isDepositsGreaterThanSpends(UUID userId, String productType) {
        BigDecimal deposits = getTotalDepositsAmount(userId, productType);
        BigDecimal spends = getTotalSpendsAmount(userId, productType);
        return deposits.compareTo(spends) > 0;
    }
}
