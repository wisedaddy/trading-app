package sfilyuta.trade.processor;

import org.junit.Test;
import sfilyuta.trade.domain.Order;

import java.math.BigDecimal;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sfilyuta.trade.domain.Direction.SELL;
import static sfilyuta.trade.support.OrderBuilder.anOrder;

public class SellOrdersProviderTest {

    @Test
    public void calculateBuyOrdersAmountWithSamePrice() {
        SellOrdersProvider sellOrdersProvider = new SellOrdersProvider(of(
                anOrder().buy().withAmount(18).withPrice( "10.10").build(),
                anOrder().sell().withAmount(33).withPrice("10.10").build(),
                anOrder().sell().withAmount(32).withPrice( "10.10").build()
        ));
        assertThat(sellOrdersProvider.getSellOrdersSortedByPrice()).containsExactly(
                new Order(SELL, 65, new BigDecimal("10.10"))
        );
    }

    @Test
    public void calculateOrdersAmountWithDifferentPrice() {
        SellOrdersProvider sellOrdersProvider = new SellOrdersProvider(of(
                anOrder().buy().withAmount(18).withPrice("10.30").build(),
                anOrder().sell().withAmount(33).withPrice("10.10").build(),
                anOrder().sell().withAmount(32).withPrice("10.50").build()
        ));
        assertThat(sellOrdersProvider.getSellOrdersSortedByPrice()).containsExactly(
                anOrder().sell().withAmount(33).withPrice("10.10").build(),
                anOrder().sell().withAmount(32).withPrice("10.50").build()
        );
    }

    @Test
    public void throwsExceptionWhenOrdersIsNull() throws Exception {
        assertThatThrownBy(() -> new SellOrdersProvider(null)).hasMessage("Orders list cannot be null");
    }
}
