package com.ozz.demo.date;

import java.util.Calendar;

public class CalendarUtil {

  public Calendar addDay(Calendar cal, int day) {
    cal.add(Calendar.DAY_OF_MONTH, day);
    return cal;
  }

  public Calendar setTime(Calendar cal, int hourOfDay, int minute, int second) {
    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, second);
    return cal;
  }

}
