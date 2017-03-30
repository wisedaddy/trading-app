package sfilyuta.trade.processor;

import org.junit.Test;

import java.math.BigDecimal;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sfilyuta.trade.support.OrderBuilder.anOrder;

public class BuyOrdersProviderTest {

    @Test
    public void calculateBuyOrdersAmountWithSamePrice() {
        BuyOrdersProvider buyOrdersProvider = new BuyOrdersProvider(of(
                anOrder().buy().withAmount(12).withPrice("10.10").build(),
                anOrder().buy().withAmount(8).withPrice("10.10").build(),
                anOrder().sell().withAmount(33).withPrice("10.10").build()
        ));
        assertThat(buyOrdersProvider.ordersForStartPrice(new BigDecimal("10.10")).values().iterator().next())
                .isEqualTo(20);
    }

    @Test
    public void calculateOrdersAmountWithDifferentPrice() {
        BuyOrdersProvider buyOrdersProvider = new BuyOrdersProvider(of(
                anOrder().buy().withAmount(12).withPrice("10.10").build(),
                anOrder().buy().withAmount(18).withPrice("10.30").build(),
                anOrder().sell().withAmount(33).withPrice("10.10").build()
        ));
        assertThat(buyOrdersProvider.ordersForStartPrice(new BigDecimal("10.10")).values().iterator().next())
                .isEqualTo(12);
    }

    @Test
    public void throwsExceptionWhenOrdersIsNull() throws Exception {
        assertThatThrownBy(() -> new BuyOrdersProvider(null)).hasMessage("Orders list cannot be null");
    }

}
