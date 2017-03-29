package sfilyuta.trade.processor;

import sfilyuta.trade.domain.Order;
import sfilyuta.trade.domain.TradeResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.lang.Math.min;
import static sfilyuta.trade.domain.TradeResult.NA;

public class PriceCalculator {

    private List<Order> orders;

    public PriceCalculator(List<Order> orders) {
        this.orders = orders;
    }

    public TradeResult tradeResult() {
        TreeMap<Integer, Set<BigDecimal>> trades = new TreeMap<>();
        SellOrdersProvider sellOrdersProvider = new SellOrdersProvider(orders);
        BuyOrdersProvider buyOrdersProvider = new BuyOrdersProvider(orders);
        int sellTotalAmount = 0;

        for (Order order : sellOrdersProvider.getSellOrdersSortedByPrice()) {
            sellTotalAmount += order.getAmount();
            Integer matchedOrderAmount = min(sellTotalAmount,
                    buyOrdersProvider.orderAmountForStartingPrice(order.getPrice()));

            if (matchedOrderAmount > 0) {
                updateTrades(trades, order.getPrice(), matchedOrderAmount);
            }
        }

        if (trades.isEmpty()) {
            return NA;
        }

        Map.Entry<Integer, Set<BigDecimal>> topTrade = trades.lastEntry();
        return new TradeResult(topTrade.getKey(), avgPrice(topTrade.getValue()));
    }

    private void updateTrades(TreeMap<Integer, Set<BigDecimal>> trades, BigDecimal price, Integer minAmount) {
        Set<BigDecimal> priceSet = trades.get(minAmount);

        if (priceSet == null) {
            priceSet = new HashSet<>();
        }

        priceSet.add(price);
        trades.put(minAmount, priceSet);
    }

    private BigDecimal avgPrice(Set<BigDecimal> prices) {
        BigDecimal avgPrice = prices.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        avgPrice = avgPrice.divide(new BigDecimal(prices.size()), RoundingMode.CEILING);
        return avgPrice;
    }

}
