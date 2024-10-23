package pl.com.chrzanowski.sma.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;


public class DateTimeUtil {

    public static Instant setDateTimeIfNotExists(Instant localDateTime) {
        if (localDateTime != null) {
            return localDateTime;
        }
        return Instant.now();
    }

    public static Date calculateExpiryDate(int expiryDateInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryDateInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
