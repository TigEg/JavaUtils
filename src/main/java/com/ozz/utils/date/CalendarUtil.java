package com.ozz.utils.date;

import java.util.Calendar;

public class CalendarUtil {

    public static Calendar addDay(Calendar cal, int day) {
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal;
    }

    public static Calendar setTime(Calendar cal, int hourOfDay, int minute, int second) {
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return cal;
    }

}
