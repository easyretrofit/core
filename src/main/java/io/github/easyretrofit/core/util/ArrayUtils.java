package io.github.easyretrofit.core.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArrayUtils {

    public static boolean hasSameElement(Object[] thisArray, Object[] otherArray) {
        Set<Object> thisSet = new HashSet<>(Arrays.asList(thisArray));
        Set<Object> otherSet = new HashSet<>(Arrays.asList(otherArray));
        return thisSet.equals(otherSet);
    }

    public static Set<Object> toSet(Object[] array) {
        return new HashSet<>(Arrays.asList(array));
    }
}
