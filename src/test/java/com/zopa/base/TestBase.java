package com.zopa.base;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.zopa.engine.Application;
import com.zopa.guice.TestModule;
import com.zopa.logic.service.InterestCalculator;
import org.testng.annotations.*;

public class TestBase {

    protected static Injector injector;
    protected static Application app;
    protected static InterestCalculator interestCalculator;

    @BeforeSuite
    public static void beforeSuite() {
        injector = Guice.createInjector(Stage.DEVELOPMENT, new TestModule());
        app = injector.getInstance(Application.class);
        interestCalculator = injector.getInstance(InterestCalculator.class);
    }


}
