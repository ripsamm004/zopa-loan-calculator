package com.zopa.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.zopa.engine.Application;
import com.zopa.logic.service.InterestCalculator;
import com.zopa.logic.service.impl.InterestCalculatorImpl;
import com.zopa.util.StringUtils;


public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        //INIT APP
        bind(Application.class).in(Singleton.class);

        //SERVICES
        bind(InterestCalculator.class).to(InterestCalculatorImpl.class).in(Singleton.class);

        //UTILS
        bind(StringUtils.class).in(Singleton.class);
    }
}
