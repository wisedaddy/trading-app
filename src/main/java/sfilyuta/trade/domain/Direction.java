package sfilyuta.trade.domain;

public enum Direction {
    SELL("S"), BUY("B");

    private final String symbol;

    Direction(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Direction fromSymbol(String symbol) {
        for (Direction direction : values()) {
            if (direction.getSymbol().equals(symbol)) {
                return direction;
            }
        }
        throw new RuntimeException("Unknown order direction symbol: " + symbol);
    }
}
