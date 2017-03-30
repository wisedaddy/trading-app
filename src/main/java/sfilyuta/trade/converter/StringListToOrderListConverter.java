package sfilyuta.trade.converter;

import sfilyuta.trade.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class StringListToOrderListConverter implements Converter<List<String>, List<Order>> {

    private Converter<String, Order> orderConverter = new StringToOrderConverter();

    @Override
    public List<Order> convert(List<String> orderStrings) {
        return requireNonNull(orderStrings, "Order list cannot be null").stream()
                .map(line -> orderConverter.convert(line))
                .collect(Collectors.toList());
    }
}
