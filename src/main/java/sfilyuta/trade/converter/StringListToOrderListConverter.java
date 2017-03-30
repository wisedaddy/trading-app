package sfilyuta.trade.converter;

import sfilyuta.trade.domain.Order;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class StringListToOrderListConverter implements Converter<List<String>, List<Order>> {

    private Converter<String, Order> orderConverter = new StringToOrderConverter();

    @Override
    public List<Order> convert(List<String> orderStrings) throws ConverterException {
        requireNonNull(orderStrings, "Order list cannot be null");
        List<Order> orders = new ArrayList<>();

        for (String line : orderStrings) {
            orders.add(orderConverter.convert(line));
        }

        return orders;
    }
}
