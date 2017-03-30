package sfilyuta.trade.processor;

import org.junit.Test;
import sfilyuta.trade.domain.TradeResult;

import java.math.BigDecimal;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;
import static sfilyuta.trade.domain.TradeResult.NA;
import static sfilyuta.trade.support.OrderBuilder.anOrder;

public class PriceCalculatorAcceptanceTest {

    @Test
    public void calculatesThatNoResultWhenBuyPriceLessThanSell() {
        PriceCalculator priceCalculator = new PriceCalculator(of(
                anOrder().buy().withAmount(100).withPrice("10.00").build(),
                anOrder().sell().withAmount(150).withPrice("10.10").build()
        ));
        assertThat(priceCalculator.tradeResult()).isEqualTo(NA);
    }

    @Test
    public void calculatesTradingResultWhenOneSellHasTwoMatchingBuys() {
        PriceCalculator priceCalculator = new PriceCalculator(of(
                anOrder().buy().withAmount(100).withPrice("15.40").build(),
                anOrder().buy().withAmount(100).withPrice("15.30").build(),
                anOrder().sell().withAmount(150).withPrice("15.30").build()
        ));
        assertThat(priceCalculator.tradeResult()).isEqualToComparingFieldByField(
                new TradeResult(150, new BigDecimal("15.30"))
        );
    }

    @Test
    public void calculatesTradingResultWhenMuplitpleSellAndBuyWithFewMatches() {
        PriceCalculator priceCalculator = new PriceCalculator(of(
                anOrder().buy().withAmount(100).withPrice("15.40").build(),
                anOrder().buy().withAmount(100).withPrice("15.30").build(),
                anOrder().sell().withAmount(150).withPrice("15.30").build(),
                anOrder().sell().withAmount(150).withPrice("15.45").build(),
                anOrder().buy().withAmount(100).withPrice("15.20").build(),
                anOrder().sell().withAmount(100).withPrice("15.40").build()
        ));
        assertThat(priceCalculator.tradeResult()).isEqualToComparingFieldByField(
                new TradeResult(150, new BigDecimal("15.30"))
        );
    }


    @Test
    public void calculatesTradingResultWhenMuplitpleSellAndBuyWithFewMatches2() {
        PriceCalculator priceCalculator = new PriceCalculator(of(
                anOrder().buy().withAmount(100).withPrice("15.40").build(),
                anOrder().buy().withAmount(900).withPrice("17.40").build(),
                anOrder().buy().withAmount(100).withPrice("15.30").build(),
                anOrder().sell().withAmount(150).withPrice("15.30").build(),
                anOrder().sell().withAmount(100).withPrice("15.40").build(),
                anOrder().sell().withAmount(150).withPrice("15.45").build(),
                anOrder().buy().withAmount(100).withPrice("15.20").build(),
                anOrder().buy().withAmount(500).withPrice("15.10").build(),
                anOrder().buy().withAmount(500).withPrice("11.50").build()
        ));
        assertThat(priceCalculator.tradeResult()).isEqualToComparingFieldByField(
                new TradeResult(400, new BigDecimal("16.43"))
        );
    }

}
