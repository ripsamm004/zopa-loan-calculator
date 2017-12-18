package com.zopa.engine;

import com.google.inject.Inject;
import com.zopa.logic.model.MarketOffer;
import com.zopa.logic.model.Quote;
import com.zopa.logic.service.InterestCalculator;
import com.zopa.util.StringUtils;
import com.zopa.util.exception.ZopaException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.omg.CORBA.SystemException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Application {

    private final Logger LOG = Logger.getLogger(Application.class.getName());
    private final int minAmount = 1000;
    private final int maxAmount = 15000;
    private final int amountMultipleOf = 100;

    private static final int Lender_Indx = 0;
    private static final int Rate_Indx = 1;
    private static final int Available_Indx = 2;
    public static final ArrayList<String> FHEADER = new ArrayList(Arrays.asList(new String[]{
            "Lender,Rate,Available"
    }));

    @Inject
    protected InterestCalculator interestCalculator;

    @Inject
    protected StringUtils stringUtils;

    public void start(String csv, String amount) throws Exception {

        LOG.log(Level.INFO, "STARTING CALCULATION");

        int loanAmount = verifyAmount(amount);

        List<MarketOffer> offers = readOfferFromCSV(csv);

        Quote quote = interestCalculator.getQuote(loanAmount, offers);

        StringBuilder sb = new StringBuilder("\n");
        sb.append("\n");
        sb.append("FINAL OUT PUT :");
        sb.append("\n");
        sb.append("Requested amount: £" + quote.getAmount());
        sb.append("\n");
        sb.append("Rate: " + quote.getInterest() + "%");
        sb.append("\n");
        sb.append("Monthly repayment: £" + quote.getMonthlyPay());
        sb.append("\n");
        sb.append("Total repayment: £" + quote.getTotalAmount());
        sb.append("\n");

        LOG.log(Level.INFO, sb.toString());

        LOG.log(Level.INFO, "FINISH CALCULATION");
    }

    private int verifyAmount(String amount) throws Exception{

        int loanAmount;

        try {
            loanAmount = Integer.parseInt(amount);
        }
        catch (Exception ex){
            throw new ZopaException("Error Amount");
        }

        if (loanAmount < minAmount || loanAmount > maxAmount)
            throw new ZopaException(String.format("Amount Should be in the range between %d and %d", minAmount, maxAmount));

        if (loanAmount % amountMultipleOf != 0)
            throw new ZopaException(String.format("Amount Should be completely divisible by %d", amountMultipleOf));

        return loanAmount;

    }

    public List<MarketOffer> readOfferFromCSV(String csv) throws Exception{

        List<MarketOffer> list = new ArrayList();

        if(!csv.endsWith(".csv")) {
            throw new ZopaException("Error. It requires a file with csv format");
        }

        File file = new File(csv);
        if(!file.exists()) {
            throw new ZopaException("NO FILE EXIST");
        }

        if(file.isDirectory()) {
            throw new ZopaException("Error. It is a directory");
        }

        try {

            BufferedReader in = new BufferedReader(new FileReader(file));

            for (String x : FHEADER) {
                in.readLine();
            }

            MarketOffer marketOffer;
            for (CSVRecord record : CSVFormat.DEFAULT.parse(in)) {
                marketOffer = new MarketOffer();
                String lender = record.get(Lender_Indx).trim();
                double rate = this.parseDouble(record.get(Rate_Indx).trim());
                int available = this.parseInt(record.get(Available_Indx).trim());
                marketOffer.setLander(lender);
                marketOffer.setRate(rate);
                marketOffer.setAvailable(available);
                LOG.log(Level.INFO, String.format("ROW : %s - %4.3f - %d", lender, rate, available));
                list.add(marketOffer);
            }

        } catch (IOException ex) {
            throw new ZopaException("Error. csv file can not process");
        }


        return (list.size()>0) ? this.sortOffers(list) : null;

    }

    private List<MarketOffer> sortOffers(List<MarketOffer> list) throws SystemException {

        Collections.sort(list, new Comparator<MarketOffer>() {
            public int compare(MarketOffer o1, MarketOffer o2) {
                MarketOffer p1 = o1;
                MarketOffer p2 = o2;
                return Double.compare(p1.getRate(), p2.getRate());
            }
        });

        return list;
    }

    private double parseDouble(String s) throws Exception{
        try {
            if (s == null || s.isEmpty() || s.equals("-") || s.trim().equals("-")) {
                return 0;
            }
            return Double.parseDouble(s.trim().replaceAll(",", ""));
        } catch (Exception e) {
            throw new ZopaException(String.format("Error. csv contain wrong rate value %s ", s));
        }
    }

    private int parseInt(String s) throws Exception{
        try {
            if (s == null || s.isEmpty() || s.equals("-") || s.trim().equals("-")) {
                return 0;
            }
            return Integer.parseInt(s.replaceAll(",", ""));
        } catch (Exception e) {
            throw new ZopaException(String.format("Error. csv contain wrong amount value %s ", s));
        }
    }
}
