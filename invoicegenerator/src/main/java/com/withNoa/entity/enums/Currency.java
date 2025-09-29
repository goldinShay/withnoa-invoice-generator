package com.withNoa.entity.enums;

public enum Currency {
    EUR("Euro", "€"),
    USD("US Dollar", "$"),
    GBP("British Pound", "£"),
    CHF("Swiss Franc", "CHF"),
    JPY("Japanese Yen", "¥");

    private final String name;
    private final String symbol;

    Currency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }
}
