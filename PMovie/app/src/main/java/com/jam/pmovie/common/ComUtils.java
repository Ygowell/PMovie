package com.jam.pmovie.common;

import java.util.Collection;

/**
 * Created by jam on 18/1/5.
 */

public class ComUtils {

    public static boolean isEmpty(Collection<?> object) {
        if (object == null) return true;

        if (object.isEmpty()) return true;

        return false;
    }
}
