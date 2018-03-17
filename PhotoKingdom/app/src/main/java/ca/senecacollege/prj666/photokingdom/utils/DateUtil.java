package ca.senecacollege.prj666.photokingdom.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Convert date string and calculate expiry date
 *
 * @author Wonho
 */

public class DateUtil {
    public static String parseDateString(String date) {
        String year = date.substring(0, date.indexOf('-'));
        String monthDays = date.substring(date.indexOf('-') + 1, date.indexOf('T'));

        return year + '\n' + monthDays;
    }

    public static String getExpiresIn(String begin, String end) {
        Date dateBegin = ISO8601toDate(begin);
        Date dateEnd = ISO8601toDate(end);
        long diff = dateEnd.getTime() - dateBegin.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        String result = "Expires in ";
        if (days > 1) {
            result += String.format("%d days", days);
        } else {
            long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
            result += String.format("%d hours", hours);
        }

        return result;
    }

    public static Date ISO8601toDate(String iso8601) {
        DateFormat dateFormat = createDateFormat();

        try {
            return dateFormat.parse(iso8601);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String dateToISO8601(Date date) {
        DateFormat dateFormat = createDateFormat();
        return dateFormat.format(date);
    }

    private static DateFormat createDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return dateFormat;
    }
}