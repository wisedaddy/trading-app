package sfilyuta.trade.converter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sfilyuta.trade.support.OrderBuilder.anOrder;

public class StringToOrderConverterTest {

    private StringToOrderConverter converter = new StringToOrderConverter();

    @Test
    public void parsesBuyOrder() throws Exception {
        assertThat(converter.convert("B 12 10.05")).isEqualToComparingFieldByField(
                anOrder().buy().withAmount(12).withPrice("10.05").build()
        );
    }

    @Test
    public void parsesSellOrder() throws Exception {
        assertThat(converter.convert("S 7623 5346.91")).isEqualToComparingFieldByField(
                anOrder().sell().withAmount(7623).withPrice("5346.91").build()
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