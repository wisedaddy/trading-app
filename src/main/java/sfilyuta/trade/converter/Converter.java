package sfilyuta.trade.converter;

public interface Converter<A, B> {
    B convert(A src);
}
