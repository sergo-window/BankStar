package org.skypro.recommendation_service.model.enums;

import java.math.BigDecimal;

public enum ComparisonOperator {
    GREATER_THAN(">"),
    LESS_THAN("<"),
    EQUALS("="),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN_OR_EQUALS("<=");

    private final String symbol;

    ComparisonOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static ComparisonOperator fromSymbol(String symbol) {
        for (ComparisonOperator operator : values()) {
            if (operator.symbol.equals(symbol)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown comparison operator: " + symbol);
    }

    public boolean compare(BigDecimal value1, BigDecimal value2) {
        return switch (this) {
            case GREATER_THAN -> value1.compareTo(value2) > 0;
            case LESS_THAN -> value1.compareTo(value2) < 0;
            case EQUALS -> value1.compareTo(value2) == 0;
            case GREATER_THAN_OR_EQUALS -> value1.compareTo(value2) >= 0;
            case LESS_THAN_OR_EQUALS -> value1.compareTo(value2) <= 0;
        };
    }
}
