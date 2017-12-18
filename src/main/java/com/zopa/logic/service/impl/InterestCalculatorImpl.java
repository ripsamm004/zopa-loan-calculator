package com.zopa.logic.service.impl;

import com.zopa.logic.model.MarketOffer;
import com.zopa.logic.model.Quote;
import com.zopa.logic.service.InterestCalculator;
import com.zopa.util.exception.ZopaException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * COMPOUND INTEREST FORMULA:   A = P(1+r/n)^nt  FOR ZOPA I HAVE TO CHANGE IT TO A = P(0.9970221+r/n)^nt
 * where
 * P = loan amount, r = rateInDecimal, t = number of year,
 * n = number of compound per unit 12 month and A = Total to pay by borrower
 */

public class InterestCalculatorImpl implements InterestCalculator {

    private final Logger LOG = Logger.getLogger(InterestCalculatorImpl.class.getName());
    private final int LOAN_FOR_MONTH = 36;
    private final int AMOUNT_DECIMAL_PLACE = 2;
    private final int RATE_DECIMAL_PLACE = 1;
    private final double fixZopaFraction = 0.9970221d;  // THIS FIGURE IS FOR ZOPA LOAN CALCULATOR
    private final int n = 12;
    private final int t  = 3;

    public Quote getQuote(int amount, List<MarketOffer> offers) throws Exception {


        if (null == offers)
        {
            throw new ZopaException("NO OFFER AVAILABLE");
        }

        int availableAmountFromLander = 0;

        for (MarketOffer offer : offers) {
            availableAmountFromLander += offer.getAvailable();
        }

        if (availableAmountFromLander < amount)
        {
            throw new ZopaException("LOAN AMOUNT NOT AVAILABLE");
        }

        double totalAmountFromLander = CalculateAmountFromLander(amount, offers);

        LOG.log(Level.INFO,  "TOTAL AMOUNT FROM LANDER : " + totalAmountFromLander);

        double rateInPercentage = (totalAmountFromLander - amount) / amount * 100;

        BigDecimal bd = new BigDecimal(Double.toString(rateInPercentage));
        bd = bd.setScale(RATE_DECIMAL_PLACE, RoundingMode.HALF_UP);
        rateInPercentage = bd.doubleValue();
        LOG.log(Level.INFO,  "ZOPA RATE % : " + rateInPercentage);


        double rateInDecimal = rateInPercentage / 100;
        LOG.log(Level.INFO,  "DECIMAL RATE : " + rateInDecimal);


        // COMPOUND INTEREST IMPLEMENTATION

        double rateInDecimalDivNumOfPerionInYear = rateInDecimal / n;
        rateInDecimalDivNumOfPerionInYear += fixZopaFraction;
        BigDecimal zopaTotalLoanAmount = new BigDecimal(Double.toString(rateInDecimalDivNumOfPerionInYear)).pow(n*t);
        double finalLoanAmountWithInterest = amount * zopaTotalLoanAmount.doubleValue();


        BigDecimal cleanFinalLoanAmount = new BigDecimal(Double.toString(finalLoanAmountWithInterest));
        cleanFinalLoanAmount = cleanFinalLoanAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

        double monthlyRepayment = cleanFinalLoanAmount.doubleValue() / LOAN_FOR_MONTH;

        DecimalFormat df1 = new DecimalFormat("#.0");
        DecimalFormat df2 = new DecimalFormat("#.00");
        LOG.log(Level.INFO,  "TEST VALUE : " + df2.format(cleanFinalLoanAmount.doubleValue()));

        Quote quote = new Quote();
        quote.setAmount(amount);
        quote.setInterest(df1.format(rateInPercentage));
        quote.setMonthlyPay(df2.format(monthlyRepayment));
        quote.setTotalAmount(df2.format(cleanFinalLoanAmount.doubleValue()));

        return quote;

    }

    private double CalculateAmountFromLander(int amount, List<MarketOffer> offers)
    {
        double totalAmountFromLander = 0.0D;
        double amountFromLander = 0.0D;
        int borrowAmountFromLander   = 0;

        for (MarketOffer offer : offers) {
            int amountToBorrow = amount < borrowAmountFromLander + offer.getAvailable() ? amount - borrowAmountFromLander : offer.getAvailable();
            amountFromLander = amountToBorrow + (amountToBorrow * offer.getRate());
            totalAmountFromLander +=amountFromLander;
            borrowAmountFromLander += amountToBorrow;

            LOG.log(Level.INFO, "BORROW FROM LANDER : " + offer.getLander() + " AMOUNT : " + amountToBorrow + " INTEREST AMOUNT : " + amountFromLander );

            if (borrowAmountFromLander >= amount)
            {
                break;
            }
        }

        BigDecimal bd = new BigDecimal(Double.toString(totalAmountFromLander));
        bd = bd.setScale(AMOUNT_DECIMAL_PLACE, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
