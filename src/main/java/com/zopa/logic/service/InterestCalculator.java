package com.zopa.logic.service;

import com.zopa.logic.model.MarketOffer;
import com.zopa.logic.model.Quote;

import java.util.List;

public interface InterestCalculator {

    Quote getQuote(int amount, List<MarketOffer> offers) throws Exception;

}


