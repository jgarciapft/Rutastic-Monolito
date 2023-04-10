package helper;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date and time utility class
 */
public class DateTimeUtils {

    /**
     * A shorthand for {@link #formatDate(Date, TimeZone)} when you don't need to specify a time zone, so it uses
     * the system's default one
     *
     * @param date Date object storing a date
     * @return A capitalized string that represents the date object. It contains the day of the week, day of the month,
     * name of the month and the timestamp. Equivalent to the format 'dd MMMM yyyy - HH:mm'
     */
    public static String formatDate(Date date) {
        return formatDate(date, TimeZone.getTimeZone(ZoneId.systemDefault()));
    }

    /**
     * @param date     Date object storing a date
     * @param timeZone Adjust the date for a specific time zone
     * @return A capitalized string that represents the date object. It contains the day of the week, day of the month,
     * name of the month and the timestamp. Equivalent to the format 'dd MMMM yyyy - HH:mm'
     */
    public static String formatDate(Date date, TimeZone timeZone) {
        SimpleDateFormat dateFormatter = getDateFormatter();
        dateFormatter.setTimeZone(timeZone);

        // Only format date if it isn't null
        return date != null ? dateFormatter.format(date).toUpperCase() : "EMPTY_TIMEDATE";
    }

    /**
     * A shorthand for {@link #formatEpochTime(long, int, TimeZone)}} when you don't need to specify a time zone,
     * so it uses the system's default one
     *
     * @param units      UNIX timestamp
     * @param resolution Resolution of the timestamp, SECONDS or MILLISECONDS
     * @return A string that represents the date object in the format 'dd MMMM yyyy - HH:mm'
     */
    public static String formatEpochTime(long units, int resolution) {
        return formatEpochTime(units, resolution, TimeZone.getTimeZone(ZoneId.systemDefault()));
    }

    /**
     * @param units      UNIX timestamp
     * @param timeZone   Adjust the timestamp for a specific time zone
     * @param resolution Resolution of the timestamp, SECONDS or MILLISECONDS
     * @return A string that represents the date object in the format 'dd MMMM yyyy - HH:mm'
     */
    public static String formatEpochTime(long units, int resolution, TimeZone timeZone) {
        // Date objects can only take UNIX timestamps expressed in milliseconds, so do the conversion when dealing with seconds
        return formatDate(new Date(resolution == TimeResolution.SECONDS ? units * 1000L : units), timeZone);
    }

        /**
         * @return The Simple Date Format object used to format dates
         */
    public static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("dd MMMM yyyy - HH:mm");
    }

    /**
     * Constants for specifying the scale of a UNIX timestamp
     */
    public static class TimeResolution {
        public static final int MILLISECONDS = 1;
        public static final int SECONDS = 2;
    }

}