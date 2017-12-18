package com.zopa.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**ø
 * @author shiful
 */
public class StringUtils {

    private final Logger LOG = Logger.getLogger(StringUtils.class.getName());

    public boolean isEmptyOrNull(String val){
        LOG.log(Level.INFO, "UTIL");
        return val == null || val.isEmpty();
    }


}
