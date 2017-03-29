package sfilyuta.trade.domain;

import java.math.BigDecimal;

public class Order {
    private Direction direction;
    private int amount;
    private BigDecimal price;

    public Order(Direction direction, int amount, BigDecimal price) {
        this.direction = direction;
        this.amount = amount;
        this.price = price;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "direction=" + direction +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (amount != order.amount) return false;
        if (direction != order.direction) return false;
        return price.equals(order.price);
    }

    @Override
    public int hashCode() {
        int result = direction.hashCode();
        result = 31 * result + amount;
        result = 31 * result + price.hashCode();
        return result;
    }
}
