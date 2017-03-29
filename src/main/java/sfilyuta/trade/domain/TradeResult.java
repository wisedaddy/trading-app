package sfilyuta.trade.domain;

import java.math.BigDecimal;

public class TradeResult {

    public static final TradeResult NA = new TradeResult(0, null);

    private int amount;
    private BigDecimal price;

    public TradeResult(int amount, BigDecimal price) {
        this.amount = amount;
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
