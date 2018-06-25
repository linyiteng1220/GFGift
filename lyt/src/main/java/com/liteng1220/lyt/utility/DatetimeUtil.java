package com.liteng1220.lyt.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatetimeUtil {

    public static int getCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getTimeHour(String datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(convertDatetime2Timestamp(datetime));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String getCurrentDatetime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return simpleDateFormat.format(new Date());
    }

    public static String convertTimestamp2Datetime(long timestamp) {
        return convertTimestamp2Datetime(timestamp, "yyyy-MM-dd HH:mm:ss");
    }

    public static String convertTimestamp2Datetime(long timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return simpleDateFormat.format(new Date(timestamp));
    }

    public static long convertDatetime2Timestamp(String datetime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = simpleDateFormat.parse(datetime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
