package ec.ttbmd.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeUtil {

    private static final String TIME_PATTERN = "HH'H'mm";

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    private static final String CUSTOM_PATTERN = "HH':'mm";

    private static final DateTimeFormatter NEW_FORMATTER = DateTimeFormatter.ofPattern(CUSTOM_PATTERN);

    public static String format(LocalTime time) {
        if (time == null) {
            return null;
        }
        return TIME_FORMATTER.format(time);
    }

    public static String new_format(LocalTime time) {
        if (time == null) {
            return null;
        }
        return NEW_FORMATTER.format(time);
    }

    public static LocalTime parse(String timeString) {
        try {
            return TIME_FORMATTER.parse(timeString, LocalTime::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalTime new_parse(String timeString) {
        try {
            return NEW_FORMATTER.parse(timeString, LocalTime::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean validTime(String timeString) {
        
        return TimeUtil.new_parse(timeString) != null;
    }
    
    public static int toMinutes(LocalTime time) {
        return time.getMinute() + time.getHour() * 60;
    }
    
    public static double timeDiff(LocalTime time1, LocalTime time2) {
        return (toMinutes(time2) - toMinutes(time1));
    }
}
