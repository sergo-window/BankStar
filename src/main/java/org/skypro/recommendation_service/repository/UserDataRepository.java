package org.skypro.recommendation_service.repository;

import org.skypro.recommendation_service.model.enums.OperationType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDataRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserDataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID findUserIdByName(String username) {
        String sql = """
                SELECT id 
                FROM bank_users 
                WHERE CONCAT(first_name, ' ', last_name) ILIKE ?
                """;

        List<UUID> results = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> UUID.fromString(rs.getString("id")),
                username
        );

        return results.size() == 1 ? results.get(0) : null;
    }

    public String getUserNameById(UUID userId) {
        String sql = """
                SELECT first_name, last_name 
                FROM bank_users 
                WHERE id = ?
                """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        return firstName + " " + lastName;
                    },
                    userId
            );
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasProductType(UUID userId, String productType) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM user_operations uo
                JOIN bank_products bp ON uo.product_id = bp.id
                WHERE uo.user_id = ? AND bp.type = ?
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType));
    }

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

    public BigDecimal getTotalDepositsAmount(UUID userId, String productType) {
        return getTotalAmount(userId, productType, OperationType.DEPOSIT);
    }

    public BigDecimal getTotalSpendsAmount(UUID userId, String productType) {
        return getTotalAmount(userId, productType, OperationType.SPEND);
    }

    public boolean isDepositsGreaterThanSpends(UUID userId, String productType) {
        BigDecimal deposits = getTotalDepositsAmount(userId, productType);
        BigDecimal spends = getTotalSpendsAmount(userId, productType);
        return deposits.compareTo(spends) > 0;
    }
}
