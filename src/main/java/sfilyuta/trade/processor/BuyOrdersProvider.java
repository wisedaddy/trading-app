package sfilyuta.trade.processor;

import sfilyuta.trade.domain.Order;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.requireNonNull;
import static sfilyuta.trade.domain.Direction.BUY;

public class BuyOrdersProvider {

    private TreeMap<BigDecimal, Integer> buyOrders = new TreeMap<>(Collections.reverseOrder());

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

        int amount = 0;
        for (Map.Entry<BigDecimal, Integer> order : buyOrders.entrySet()) {
            amount = amount + order.getValue();
            order.setValue(amount);
        }
    }

    private boolean isBuy(Order order) {
        return order.getDirection() == BUY;
    }

    public int orderAmountForStartingPrice(BigDecimal startPrice) {
        Map.Entry<BigDecimal, Integer> amountEntry = buyOrders.floorEntry(startPrice);
        return amountEntry == null ? 0 : amountEntry.getValue();
    }

}
