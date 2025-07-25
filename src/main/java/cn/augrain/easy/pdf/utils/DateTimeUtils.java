package cn.augrain.easy.pdf.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author biaoy
 * @since 2025/07/25
 */
public class DateTimeUtils {

    public static Calendar toCalendar(LocalDateTime dateTime) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(toDate(dateTime));
        return instance;
    }

    public static Date toDate(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = dateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }
}
