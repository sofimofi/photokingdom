package ca.senecacollege.prj666.photokingdom.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Convert date string and calculate expiry date
 *
 * @author Wonho, sofia
 */

public class DateUtil {
    public static String parseDateString(String date) {
        String monthDays = date.substring(date.indexOf('-') + 1, date.indexOf('T'));

        return monthDays;
    }

    public static String getExpiresIn(String end) {
        // Now
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        // Expiry
        Date dateEnd = ISO8601toDate(end);

        long diff = dateEnd.getTime() - now.getTime();
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

    /**
     * Determines if an ISO8601 date string is before current datetime
     * @param iso8601
     * @return true if date string is before now
     */
    public static boolean isBeforeNow(String iso8601){
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Date date = ISO8601toDate(iso8601);
        return date.before(now);
    }

    /**
     * Turns iso8601 string to a presentable simple date string for View
     * @param iso8601
     * @return date in "Jan 4 2018" format
     */
    public static String ISO8601ToSimpleDateString(String iso8601){
        Date date = ISO8601toDate(iso8601);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d yyyy");
        return formatter.format(date);
    }

    /**
     * Turns iso8601 string to a presentable date & time string for View
     * @param iso8601
     * @return date in "Mon, Jan 4 2018 at 09:04:01" format
     */
    public static String ISO8601ToLongDateAndTimeString(String iso8601){
        Date date = ISO8601toDate(iso8601);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d yyyy 'at' HH:mm:ss");
        return formatter.format(date);
    }
}