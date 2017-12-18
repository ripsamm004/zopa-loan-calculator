package com.zopa.engine;

import com.google.inject.Injector;

public class InjectorDS {


    private static Injector injector;

    public static Injector getInjector() {
        return injector;
    }

    public static void setInjector(Injector injector) {
        synchronized (InjectorDS.class){
            InjectorDS.injector = injector;
        }

    }
}
