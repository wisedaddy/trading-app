package sfilyuta.trade.processor;

import sfilyuta.trade.domain.Order;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static sfilyuta.trade.domain.Direction.BUY;

public class BuyOrdersProvider {

    private TreeMap<BigDecimal, Integer> buyOrders = new TreeMap<>();

    public BuyOrdersProvider(List<Order> orders) {
        requireNonNull(orders, "Orders list cannot be null");
        orders.stream()
                .filter(this::isBuy)
                .forEach(order ->
                        buyOrders.put(
                                order.getPrice(),
                                buyOrders.getOrDefault(order.getPrice(), 0) + order.getAmount()
                        )
                );
    }

    private boolean isBuy(Order order) {
        return order.getDirection() == BUY;
    }

    public SortedMap<BigDecimal, Integer> ordersForStartingPrice(BigDecimal startPrice) {
        return buyOrders.tailMap(startPrice);
    }

}
