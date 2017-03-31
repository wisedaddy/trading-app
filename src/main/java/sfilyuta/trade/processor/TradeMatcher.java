package sfilyuta.trade.processor;

import sfilyuta.trade.domain.Order;
import sfilyuta.trade.domain.TradeResult;
import sfilyuta.trade.validator.OrderValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Math.min;

public class TradeMatcher {

    private final List<Order> orders;

    private int maxAmount = 0;
    private Set<BigDecimal> resPrices = new HashSet<>();

    public TradeMatcher(List<Order> orders) {
        OrderValidator orderValidator = new OrderValidator();
        orderValidator.checkValid(orders);
        this.orders = orders;
    }

    public TradeResult tradeResult() {
        SellOrdersProvider sellOrdersProvider = new SellOrdersProvider(orders);
        BuyOrdersProvider buyOrdersProvider = new BuyOrdersProvider(orders);
        int sellTotalAmount = 0;

        for (Order sellOrder : sellOrdersProvider.ordersSortedByPrice()) {
            sellTotalAmount += sellOrder.getAmount();
            SortedMap<BigDecimal, Integer> buyOrders = buyOrdersProvider.ordersForStartPrice(sellOrder.getPrice());
            Set<BigDecimal> buyOrderPrices = buyOrders.keySet();

            for (BigDecimal buyPrice : buyOrderPrices) {
                int buyTotalAmount = buyOrders.tailMap(buyPrice).values().stream().mapToInt(Integer::intValue).sum();
                Integer matchedOrderAmount = min(sellTotalAmount, buyTotalAmount);

                if (matchedOrderAmount > 0) {
                    updateResults(newHashSet(buyPrice, sellOrder.getPrice()), matchedOrderAmount);
                }
            }
        }

        return TradeResult.of(maxAmount, avgPrice(resPrices));
    }

    private void updateResults(Set<BigDecimal> prices, Integer amount) {
        if (amount >= maxAmount) {

            if (amount > maxAmount) {
                resPrices = new HashSet<>();
            }

            resPrices.addAll(prices);
            maxAmount = amount;
        }
    }

    private BigDecimal avgPrice(Set<BigDecimal> prices) {
        switch (prices.size()) {
            case 0:
                return null;

            case 1:
                return prices.iterator().next();

            default:
                return prices.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(prices.size()), 2, RoundingMode.CEILING);
        }
    }

}
