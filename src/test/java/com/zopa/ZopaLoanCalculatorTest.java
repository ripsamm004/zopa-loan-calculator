package com.zopa;


import com.zopa.base.TestBase;
import com.zopa.logic.model.MarketOffer;
import com.zopa.logic.model.Quote;
import com.zopa.util.exception.ZopaException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class ZopaLoanCalculatorTest extends TestBase {

    @Test(priority = 1)
    public void getQuote() throws Exception {

        List<MarketOffer> offers = new ArrayList();

        MarketOffer marketOffer = new MarketOffer();
        marketOffer.setLander("");
        marketOffer.setRate(0.01d);
        marketOffer.setAvailable(400);
        offers.add(marketOffer);
        String msg = "";
        try {

            Quote quote = interestCalculator.getQuote(1000, offers);
        }
        catch (ZopaException zpx) {
            msg = zpx.getBeautyMessage();
        }
        catch (Exception ex) {

        }

        Assert.assertTrue(msg.contains("LOAN AMOUNT NOT AVAILABLE"));
    }


    @Test(priority = 1)
    public void getZopaOutPut() throws Exception {

        List<MarketOffer> offers = app.readOfferFromCSV("data/market.csv");
        String msg = "";
        try {

            Quote quote = interestCalculator.getQuote(1000, offers);
            Assert.assertEquals(quote.getAmount(), 1000);
            Assert.assertEquals(quote.getInterest(), "7.0");
            Assert.assertEquals(quote.getMonthlyPay(), "30.78");
            Assert.assertEquals(quote.getTotalAmount(), "1108.10");
        }
        catch (ZopaException zpx) {
            msg = zpx.getBeautyMessage();
        }
        catch (Exception ex) {

        }

    }

}
