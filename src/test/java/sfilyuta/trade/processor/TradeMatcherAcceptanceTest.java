package sfilyuta.trade.processor;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;
import sfilyuta.trade.domain.Order;
import sfilyuta.trade.domain.TradeResult;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import static sfilyuta.trade.domain.TradeResult.NA;
import static sfilyuta.trade.support.OrderBuilder.anOrder;

@RunWith(JUnitParamsRunner.class)
public class TradeMatcherAcceptanceTest {

    @Test
    @Parameters(method = "tradeParameters")
    @TestCaseName("{0}")
    public void test(String name, List<Order> orders, TradeResult tradeResult) {
        assertThat(new TradeMatcher(orders).tradeResult()).isEqualToComparingFieldByField(tradeResult);
    }

    public Object tradeParameters() {
        return $(
                $("no result when buy price less than sell", of(
                        anOrder().buy().withAmount(100).withPrice("10.00").build(),
                        anOrder().sell().withAmount(150).withPrice("10.10").build()
                ), NA),
                $("one sell has 2 matching buys", of(
                        anOrder().buy().withAmount(100).withPrice("15.40").build(),
                        anOrder().buy().withAmount(100).withPrice("15.30").build(),
                        anOrder().sell().withAmount(150).withPrice("15.30").build()
                        ), new TradeResult(150, new BigDecimal("15.30"))
                ),
                $("multiple sell and buy with few matches", of(
                        anOrder().buy().withAmount(100).withPrice("15.40").build(),
                        anOrder().buy().withAmount(900).withPrice("17.40").build(),
                        anOrder().buy().withAmount(100).withPrice("15.30").build(),
                        anOrder().sell().withAmount(150).withPrice("15.30").build(),
                        anOrder().sell().withAmount(100).withPrice("15.40").build(),
                        anOrder().sell().withAmount(150).withPrice("15.45").build(),
                        anOrder().buy().withAmount(100).withPrice("15.20").build(),
                        anOrder().buy().withAmount(500).withPrice("15.10").build(),
                        anOrder().buy().withAmount(500).withPrice("11.50").build()
                ), new TradeResult(400, new BigDecimal("16.43"))),
                $("multiple sell and buy with few matches - 3", of(
                        anOrder().buy().withAmount(100).withPrice("15.40").build(),
                        anOrder().buy().withAmount(900).withPrice("17.40").build(),
                        anOrder().buy().withAmount(900).withPrice("19.40").build(),
                        anOrder().buy().withAmount(100).withPrice("15.30").build(),
                        anOrder().sell().withAmount(700).withPrice("15.45").build(),
                        anOrder().buy().withAmount(100).withPrice("15.20").build(),
                        anOrder().buy().withAmount(500).withPrice("15.10").build(),
                        anOrder().buy().withAmount(500).withPrice("11.50").build()
                ), new TradeResult(700, new BigDecimal("17.42"))),
                $("multiple sell and buy with few matches - 2", of(
                        anOrder().buy().withAmount(100).withPrice("15.40").build(),
                        anOrder().buy().withAmount(100).withPrice("15.30").build(),
                        anOrder().sell().withAmount(150).withPrice("15.30").build(),
                        anOrder().sell().withAmount(150).withPrice("15.45").build(),
                        anOrder().buy().withAmount(100).withPrice("15.20").build(),
                        anOrder().sell().withAmount(100).withPrice("15.40").build()
                ), new TradeResult(150, new BigDecimal("15.30")))
        );
    }


}
