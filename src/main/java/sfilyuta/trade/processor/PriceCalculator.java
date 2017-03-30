package sfilyuta.trade.processor;

import sfilyuta.trade.domain.Order;
import sfilyuta.trade.domain.TradeResult;
import sfilyuta.trade.validator.OrderValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.lang.Math.min;

public class PriceCalculator {

    private OrderValidator orderValidator = new OrderValidator();

    private final List<Order> orders;

    private int maxAmount = 0;
    private Set<BigDecimal> resPrices = new HashSet<>();

    public PriceCalculator(List<Order> orders) {
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
            boolean firstBuyOrderMatch = true;
            for (BigDecimal buyPrice : buyOrderPrices) {
                int buyTotalAmount = calcBuyTotalAmount(buyOrders, buyPrice, sellTotalAmount);
                Integer matchedOrderAmount = min(sellTotalAmount, buyTotalAmount);

                if (matchedOrderAmount > 0) {
                    updateResult(buyPrice, matchedOrderAmount);

                    if (firstBuyOrderMatch && !sellOrder.getPrice().equals(buyPrice)) {
                        updateResult(sellOrder.getPrice(), matchedOrderAmount);
                    }
                }

                firstBuyOrderMatch = false;
            }
        }

        return TradeResult.of(maxAmount, avgPrice(resPrices));
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

    private void updateResult(BigDecimal price, Integer amount) {
        if (amount >= maxAmount) {
            if (amount > maxAmount) {
                resPrices = new HashSet<>();
            }
            resPrices.add(price);
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
                BigDecimal avgPrice = prices.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                avgPrice = avgPrice.divide(new BigDecimal(prices.size()), RoundingMode.CEILING);
                return avgPrice;
        }
    }

}
