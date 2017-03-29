package sfilyuta.trade.processor;

import org.junit.Test;
import sfilyuta.trade.domain.Order;

import java.math.BigDecimal;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sfilyuta.trade.domain.Direction.BUY;
import static sfilyuta.trade.domain.Direction.SELL;

public class BuyOrdersProviderTest {

    @Test
    public void calculateBuyOrdersAmountWithSamePrice() {
        BuyOrdersProvider buyOrdersProvider = new BuyOrdersProvider(of(
                new Order(BUY, 12, new BigDecimal("10.10")),
                new Order(BUY, 8, new BigDecimal("10.10")),
                new Order(SELL, 33, new BigDecimal("10.10"))
        ));
        assertThat(buyOrdersProvider.orderAmountForStartingPrice(new BigDecimal("10.10"))).isEqualTo(20);
    }

    @Test
    public void calculateOrdersAmountWithDifferentPrice() {
        BuyOrdersProvider buyOrdersProvider = new BuyOrdersProvider(of(
                new Order(BUY, 12, new BigDecimal("10.10")),
                new Order(BUY, 18, new BigDecimal("10.30")),
                new Order(SELL, 33, new BigDecimal("10.10"))
        ));
        assertThat(buyOrdersProvider.orderAmountForStartingPrice(new BigDecimal("10.10"))).isEqualTo(30);
    }

    @Test
    public void throwsExceptionWhenOrdersIsNull() throws Exception {
        assertThatThrownBy(() -> new BuyOrdersProvider(null)).hasMessage("Orders list cannot be null");
    }
}
