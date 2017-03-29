package sfilyuta.trade.processor;

import org.junit.Test;
import sfilyuta.trade.domain.Order;

import java.math.BigDecimal;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sfilyuta.trade.domain.Direction.BUY;
import static sfilyuta.trade.domain.Direction.SELL;

public class SellOrdersProviderTest {

    @Test
    public void calculateBuyOrdersAmountWithSamePrice() {
        SellOrdersProvider sellOrdersProvider = new SellOrdersProvider(of(
                new Order(BUY, 18, new BigDecimal("10.10")),
                new Order(SELL, 33, new BigDecimal("10.10")),
                new Order(SELL, 32, new BigDecimal("10.10"))
        ));
        assertThat(sellOrdersProvider.getSellOrdersSortedByPrice()).containsExactly(
                new Order(SELL, 65, new BigDecimal("10.10"))
        );
    }

    @Test
    public void calculateOrdersAmountWithDifferentPrice() {
        SellOrdersProvider sellOrdersProvider = new SellOrdersProvider(of(
                new Order(BUY, 18, new BigDecimal("10.30")),
                new Order(SELL, 33, new BigDecimal("10.10")),
                new Order(SELL, 32, new BigDecimal("10.50"))
        ));
        assertThat(sellOrdersProvider.getSellOrdersSortedByPrice()).containsExactly(
                new Order(SELL, 33, new BigDecimal("10.10")),
                new Order(SELL, 32, new BigDecimal("10.50"))
        );
    }

    @Test
    public void throwsExceptionWhenOrdersIsNull() throws Exception {
        assertThatThrownBy(() -> new SellOrdersProvider(null)).hasMessage("Orders list cannot be null");
    }
}
