package sfilyuta.trade.processor;

import sfilyuta.trade.domain.Order;
import sfilyuta.trade.domain.TradeResult;
import sfilyuta.trade.validator.OrderValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.lang.Math.min;
import static sfilyuta.trade.domain.TradeResult.NA;

public class PriceCalculator {

    private OrderValidator orderValidator = new OrderValidator();

    private final List<Order> orders;

    public PriceCalculator(List<Order> orders) {
        orderValidator.checkValid(orders);
        this.orders = orders;
    }

    public TradeResult tradeResult() {
        TreeMap<Integer, Set<BigDecimal>> trades = new TreeMap<>();
        SellOrdersProvider sellOrdersProvider = new SellOrdersProvider(orders);
        BuyOrdersProvider buyOrdersProvider = new BuyOrdersProvider(orders);
        int sellTotalAmount = 0;

        for (Order sellOrder : sellOrdersProvider.ordersSortedByPrice()) {
            sellTotalAmount += sellOrder.getAmount();

            SortedMap<BigDecimal, Integer> buyOrders = buyOrdersProvider.ordersForStartPrice(sellOrder.getPrice());

            Set<BigDecimal> buyOrderPrices = buyOrders.keySet();
            boolean firstBuyOrderMatch = true;
            for (BigDecimal buyPrice : buyOrderPrices) {
                int buyTotalAmount = calcBuyTotalAmount(buyOrders, buyPrice, sellTotalAmount);
                Integer matchedOrderAmount = min(sellTotalAmount, buyTotalAmount);

                if (matchedOrderAmount > 0) {
                    updateTrades(trades, buyPrice, matchedOrderAmount);

                    if (firstBuyOrderMatch && !sellOrder.getPrice().equals(buyPrice)) {
                        updateTrades(trades, sellOrder.getPrice(), matchedOrderAmount);
                    }
                }

                firstBuyOrderMatch = false;
            }
        }

        if (trades.isEmpty()) {
            return NA;
        }

        Map.Entry<Integer, Set<BigDecimal>> topTrade = trades.lastEntry();
        return new TradeResult(topTrade.getKey(), avgPrice(topTrade.getValue()));
    }

    private int calcBuyTotalAmount(SortedMap<BigDecimal, Integer> buyOrders, BigDecimal buyPrice, int sellTotalAmount) {
        int buyTotalAmount = 0;
        SortedMap<BigDecimal, Integer> submap = buyOrders.tailMap(buyPrice);
        Iterator<Integer> submapIterator = submap.values().iterator();

        while ((buyTotalAmount < sellTotalAmount) && submapIterator.hasNext()) {
            Integer buyOrder = submapIterator.next();
            buyTotalAmount += buyOrder;
        }

        return buyTotalAmount;
    }

    private void updateTrades(TreeMap<Integer, Set<BigDecimal>> trades, BigDecimal price, Integer amount) {
        Set<BigDecimal> priceSet = trades.get(amount);

        if (priceSet == null) {
            priceSet = new HashSet<>();
        }

        priceSet.add(price);
        trades.put(amount, priceSet);
    }

    private BigDecimal avgPrice(Set<BigDecimal> prices) {
        BigDecimal avgPrice = prices.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        avgPrice = avgPrice.divide(new BigDecimal(prices.size()), RoundingMode.CEILING);
        return avgPrice;
    }

}
