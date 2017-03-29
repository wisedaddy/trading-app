package sfilyuta.trade.processor;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import sfilyuta.trade.domain.Order;
import sfilyuta.trade.domain.TradeResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static sfilyuta.trade.domain.Direction.BUY;
import static sfilyuta.trade.domain.Direction.SELL;
import static sfilyuta.trade.domain.TradeResult.NA;

public class PriceCalculatorAcceptanceTest {

    @Test
    public void calculatesThatNoResultWhenBuyPriceLessThanSell() {
        PriceCalculator priceCalculator = new PriceCalculator(ImmutableList.of(
                new Order(BUY, 100, new BigDecimal("10.00")),
                new Order(SELL, 150, new BigDecimal("10.10"))
        ));
        assertThat(priceCalculator.tradeResult()).isEqualTo(NA);
    }

    @Test
    public void calculatesTradingResultWhenOneSellHasTwoMatchingBuys() {
        PriceCalculator priceCalculator = new PriceCalculator(
                ImmutableList.of(
                        new Order(BUY, 100, new BigDecimal("15.40")),
                        new Order(BUY, 100, new BigDecimal("15.30")),
                        new Order(SELL, 150, new BigDecimal("15.30"))
                ));
        assertThat(priceCalculator.tradeResult()).isEqualToComparingFieldByField(
                new TradeResult(150, new BigDecimal("15.30"))
        );
    }

    @Test
    public void calculatesTradingResultWhenMuplitpleSellAndBuyWithFewMatches() {
        PriceCalculator priceCalculator = new PriceCalculator(
                ImmutableList.of(
                        new Order(BUY, 100, new BigDecimal("15.40")),
                        new Order(BUY, 100, new BigDecimal("15.30")),
                        new Order(SELL, 150, new BigDecimal("15.30")),
                        new Order(SELL, 150, new BigDecimal("15.45")),
                        new Order(BUY, 100, new BigDecimal("15.20")),
                        new Order(SELL, 100, new BigDecimal("15.40"))
                ));
        assertThat(priceCalculator.tradeResult()).isEqualToComparingFieldByField(
                new TradeResult(150, new BigDecimal("15.30"))
        );
    }


    @Test
    public void calculatesTradingResultWhenMuplitpleSellAndBuyWithFewMatches2() {
        PriceCalculator priceCalculator = new PriceCalculator(
                ImmutableList.of(
                        new Order(BUY, 100, new BigDecimal("15.40")),
                        new Order(BUY, 900, new BigDecimal("17.40")),
                        new Order(BUY, 100, new BigDecimal("15.30")),
                        new Order(SELL, 150, new BigDecimal("15.30")),
                        new Order(SELL, 100, new BigDecimal("15.40")),
                        new Order(SELL, 150, new BigDecimal("15.45")),
                        new Order(BUY, 100, new BigDecimal("15.20")),
                        new Order(BUY, 500, new BigDecimal("15.10")),
                        new Order(BUY, 500, new BigDecimal("11.50"))
                ));
        assertThat(priceCalculator.tradeResult()).isEqualToComparingFieldByField(
                new TradeResult(400, new BigDecimal("17.40"))
        );
    }

}
