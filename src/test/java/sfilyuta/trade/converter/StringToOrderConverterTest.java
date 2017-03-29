package sfilyuta.trade.converter;

import sfilyuta.trade.domain.Order;
import org.junit.Test;

import java.math.BigDecimal;

import static sfilyuta.trade.domain.Direction.BUY;
import static sfilyuta.trade.domain.Direction.SELL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringToOrderConverterTest {

    private StringToOrderConverter converter = new StringToOrderConverter();

    @Test
    public void parsesBuyOrder() throws Exception {
        assertThat(converter.convert("B 12 10.05")).isEqualToComparingFieldByField(
                new Order(BUY, 12, new BigDecimal("10.05"))
        );
    }

    @Test
    public void parsesSellOrder() throws Exception {
        assertThat(converter.convert("S 7623 5346.91")).isEqualToComparingFieldByField(
                new Order(SELL, 7623, new BigDecimal("5346.91"))
        );
    }

    @Test
    public void throwsExceptionWhenStringIsNull() throws Exception {
        assertThatThrownBy(() -> converter.convert(null)).hasMessage("Order line cannot be null");
    }

    @Test
    public void throwsExceptionWhenStringIsNotValid() throws Exception {
        assertThatThrownBy(() -> converter.convert("asd")).hasMessageContaining("Error parsing order line");
    }

    @Test
    public void throwsExceptionWhenDirectionIsNotCorrect() throws Exception {
        assertThatThrownBy(() -> converter.convert("D 123 123"))
                .hasMessageContaining("Unknown order direction symbol");
    }

}