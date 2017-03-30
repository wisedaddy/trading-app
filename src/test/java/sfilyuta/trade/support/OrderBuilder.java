package sfilyuta.trade.support;

import sfilyuta.trade.domain.Direction;
import sfilyuta.trade.domain.Order;

import java.math.BigDecimal;

import static sfilyuta.trade.domain.Direction.BUY;
import static sfilyuta.trade.domain.Direction.SELL;

public class OrderBuilder {

    private Direction direction;
    private int amount;
    private BigDecimal price;

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    public OrderBuilder sell() {
        this.direction = SELL;
        return this;
    }

    public OrderBuilder buy() {
        this.direction = BUY;
        return this;
    }

    public OrderBuilder withAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public OrderBuilder withPrice(String price) {
        this.price = new BigDecimal(price);
        return this;
    }

    public Order build() {
        return new Order(direction, amount, price);
    }

}
