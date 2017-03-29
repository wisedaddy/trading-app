package sfilyuta.trade.converter;

import sfilyuta.trade.domain.Order;
import sfilyuta.trade.domain.Direction;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public class StringToOrderConverter implements Converter<String, Order> {

    @Override
    public Order convert(String line) throws ConverterException {
        requireNonNull(line, "Order line cannot be null");
        String[] parts = line.split("\\s");

        if (parts.length != 3) {
            throw new RuntimeException("Error parsing order line: " + line);
        }

        Integer amount = Integer.valueOf(parts[1]);
        if (amount == 0) {
            throw new ConverterException("Incorrect amount in order");
        }

        return new Order(Direction.fromSymbol(parts[0]),
                amount,
                new BigDecimal(parts[2]));
    }

}
