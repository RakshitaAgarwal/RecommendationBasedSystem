package org.cafeteria.common.util;

public class Utils {
    public static <E extends Enum<E>> E getEnumFromOrdinal(Class<E> enumClass, int ordinal) {
        E[] enumConstants = enumClass.getEnumConstants();
        if (ordinal > 0 && ordinal <= enumConstants.length) {
            return enumConstants[ordinal-1];
        }
        throw new IllegalArgumentException("Invalid ordinal for enum " + enumClass.getSimpleName() + ": " + ordinal);
    }
}
