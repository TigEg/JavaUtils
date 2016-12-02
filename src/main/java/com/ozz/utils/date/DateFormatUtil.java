package com.ozz.utils.date;

import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 
 * 
 * @author ozz
 */
public class DateFormatUtil {

  public static String PATTERN_YEAR = "yyyy";
  public static String PATTERN_DATE = "yyyy-MM-dd";
  public static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

  public static String[] PATTERNS = new String[] {"yyyy", "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS"};

  public static String format(Date date, String pattern) {
    if (date == null) {
      return "";
    }
    return DateFormatUtils.format(date, pattern);
  }

  public static String formatDate(Date date) {
    return format(date, PATTERN_DATE);
  }

  public static String formatDateTime(Date date) {
    return format(date, PATTERN_DATETIME);
  }

  public static String formatDateTimeForTimestamp() {
    return format(new Date(), "yyyy-MM-dd HHmmss SSS");
  }

  public static Date parseDate(String date) {
    try {
      return DateUtils.parseDate(date, PATTERNS);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getTimeStringByMillis(long millis) {
    String[] modUnits = {"d", "H", "m", "s", "ms"};
    long[] mods = {24, 60, 60, 1000};

    if (millis < 0) {
      throw new RuntimeException("error millis: " + millis);
    } else if (millis == 0) {
      return 0 + modUnits[modUnits.length - 1];
    }

    long time = millis;
    String timeString = "";
    for (int i = mods.length - 1; i >= 0; i--) {
      long mod = mods[i];
      long curr = time % mod;
      time = time / mod;
      if (curr > 0 || time > 0) {
        timeString = curr + modUnits[i + 1] + timeString;
      } else {
        break;
      }
    }

    if (time > 0) {
      timeString = time + modUnits[0] + timeString;
    }

    return timeString;
  }

  public static String getStatisticsMess(long startTime, int currentNum, int totalNum) {
    int passedNum = currentNum - 1;
    long costTime = System.currentTimeMillis() - startTime;
    StringBuilder mess = new StringBuilder(String.valueOf(currentNum)).append(" of ").append(totalNum).append(", cost ").append(getTimeStringByMillis(costTime));
    if (passedNum <= 0) {
      return mess.toString();
    }

    if (totalNum >= currentNum) {
      mess.append(", left ").append(getTimeStringByMillis((totalNum - passedNum) * (costTime / passedNum)));
    }
    return mess.toString();
  }

}
