package sfilyuta.trade.validator;

import sfilyuta.trade.domain.Order;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class OrderValidator {

    private static final BigDecimal HUNDRED = new BigDecimal(100);

    public void checkValid(List<Order> orders) {
        checkOrdersCount(orders.size());
        for (Order order : orders) {
            checkPrice(order.getPrice());
            checkAmount(order.getAmount());
        }
    }

    private void checkAmount(int amount) {
        checkArgument((amount >= 1) && (amount < 1_000), "Amount of orders should be from 1 to 1000");
    }

    private void checkOrdersCount(int size) {
        checkArgument(size <= 1_000_000, "Number of orders should be <= 1 000 000");
    }

    private void checkPrice(BigDecimal price) {
        checkArgument((price.compareTo(BigDecimal.ONE) >= 0) && ((price.compareTo(HUNDRED)) <= 0) && (price.scale() <= 2),
                "Price should be from 1.00 to 100.00");
    }
}
