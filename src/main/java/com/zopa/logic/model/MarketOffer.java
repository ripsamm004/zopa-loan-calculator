package com.zopa.logic.model;


public class MarketOffer {

    private String lander;
    private double rate;
    private int available;

    public String getLander() {
        return lander;
    }

    public void setLander(String lander) {
        this.lander = lander;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
