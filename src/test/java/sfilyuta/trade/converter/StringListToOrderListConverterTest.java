package sfilyuta.trade.converter;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringListToOrderListConverterTest {

    private StringListToOrderListConverter converter = new StringListToOrderListConverter();

    @Test
    public void convert() throws Exception {
        assertThat(converter.convert(
                ImmutableList.of("S 12 15", "B 11 13")
        )).hasSize(2);
    }

    @Test
    public void throwsExceptionWhenOrderListIsNull() throws Exception {
        assertThatThrownBy(() -> converter.convert(null)).hasMessageContaining("Order list cannot be null");
    }
}