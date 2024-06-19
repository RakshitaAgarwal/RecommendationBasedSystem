package org.cafeteria.common.util;

import java.sql.Timestamp;
import java.util.Date;

public class Utils {
    public static <E extends Enum<E>> E getEnumFromOrdinal(Class<E> enumClass, int ordinal) {
        E[] enumConstants = enumClass.getEnumConstants();
        if (ordinal > 0 && ordinal <= enumConstants.length) {
            return enumConstants[ordinal-1];
        }
        throw new IllegalArgumentException("Invalid ordinal for enum " + enumClass.getSimpleName() + ": " + ordinal);
    }

    public static Timestamp dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    public static Date timestampToDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new Date(timestamp.getTime());
    }
}