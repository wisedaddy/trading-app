package sfilyuta.trade.validator;

import org.junit.Test;
import sfilyuta.trade.domain.Order;

import java.math.BigDecimal;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sfilyuta.trade.domain.Direction.SELL;

public class OrderValidatorTest {

    private OrderValidator orderValidator = new OrderValidator();

    @Test
    public void throwsExceptionWhenAmountTooLarge() {
        assertThatThrownBy(
                () -> orderValidator.checkValid(of(new Order(SELL, 1001, BigDecimal.ONE)))
        ).hasMessage("Amount of orders should be from 1 to 1000");
    }

    @Test
    public void throwsExceptionWhenAmountTooSmall() {
        assertThatThrownBy(
                () -> orderValidator.checkValid(of(new Order(SELL, 0, BigDecimal.ONE)))
        ).hasMessage("Amount of orders should be from 1 to 1000");
    }

    @Test
    public void throwsExceptionWhenPriceTooLarge() {
        assertThatThrownBy(
                () -> orderValidator.checkValid(of(new Order(SELL, 1, BigDecimal.valueOf(101))))
        ).hasMessage("Price should be from 1.00 to 100.00");
    }

    @Test
    public void throwsExceptionWhenPriceTooSmall() {
        assertThatThrownBy(
                () -> orderValidator.checkValid(of(new Order(SELL, 1, BigDecimal.valueOf(0))))
        ).hasMessage("Price should be from 1.00 to 100.00");
    }

}