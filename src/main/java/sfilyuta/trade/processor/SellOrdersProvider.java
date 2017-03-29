package sfilyuta.trade.processor;

import sfilyuta.trade.domain.Order;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import static java.util.Objects.requireNonNull;
import static sfilyuta.trade.domain.Direction.SELL;

public class SellOrdersProvider {

    private TreeMap<BigDecimal, Order> sellOrdersSortedByPrice = new TreeMap<>();

    public SellOrdersProvider(List<Order> orders) {
        requireNonNull(orders, "Orders list cannot be null")
                .stream()
                .filter(this::isSell)
                .forEach(order -> {
                    BigDecimal price = order.getPrice();
                    Order accumulatedOrder = sellOrdersSortedByPrice.get(price);

                    if (accumulatedOrder == null) {
                        accumulatedOrder = new Order(SELL, 0, price);
                    }

                    sellOrdersSortedByPrice.put(
                            price,
                            new Order(SELL, order.getAmount() + accumulatedOrder.getAmount(), price)
                    );
                });
    }

    public Collection<Order> getSellOrdersSortedByPrice() {
        return sellOrdersSortedByPrice.values();
    }

    private boolean isSell(Order order) {
        return order.getDirection() == SELL;
    }
}
