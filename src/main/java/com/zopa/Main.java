package com.zopa;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.zopa.engine.AppInjector;
import com.zopa.engine.Application;
import com.zopa.engine.InjectorDS;
import com.zopa.util.exception.ZopaException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @AUTHOR : SHIFUL
 */

public class Main {

    private final static Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args){

        try{

            if (args.length > 0 && args.length == 2) {
                LOG.log(Level.INFO, " Command Line Args  : " + args[0]);
            } else {
                throw new ZopaException("WRONG ARGUMENT PROVIDED");
            }

            Injector injector = Guice.createInjector(new AppInjector());

            InjectorDS.setInjector(injector);

            Application app = injector.getInstance(Application.class);

            LOG.log(Level.INFO, "Zopa loan calculator started  .. .. ..");

            app.start(args[0], args[1]);

            LOG.log(Level.INFO, "Zopa loan calculator stopped  .. .. ..");

        }
        catch (ZopaException zpx) {
            LOG.log(Level.SEVERE, zpx.getBeautyMessage());
            System.exit(-1);
        }
        catch(Throwable ex){
            LOG.log(Level.SEVERE, "Error. ", ex);
            System.exit(-1);
        }finally {
            System.exit(0);
        }

    }
}
