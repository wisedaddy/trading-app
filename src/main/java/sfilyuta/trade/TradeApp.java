package sfilyuta.trade;

import sfilyuta.trade.converter.StringListToOrderListConverter;
import sfilyuta.trade.domain.TradeResult;
import sfilyuta.trade.processor.TradeMatcher;
import sfilyuta.trade.ui.InputDataReader;

import static java.lang.String.format;

public class TradeApp {

    public static void main(String[] args) {
        StringListToOrderListConverter stringListToOrderListConverter = new StringListToOrderListConverter();
        InputDataReader inputDataReader = new InputDataReader();
        TradeMatcher tradeMatcher = new TradeMatcher(
                stringListToOrderListConverter.convert(inputDataReader.readFromConsole()));
        TradeResult result = tradeMatcher.tradeResult();
        output(result);
    }

    private static void output(TradeResult result) {
        if (result == null) {
            System.out.println("0 n/a");
        } else {
            System.out.println(format("%d %s", result.getAmount(), result.getPrice().toString()));
        }
    }

}
