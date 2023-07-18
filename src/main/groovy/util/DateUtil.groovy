package util

import groovy.time.TimeCategory

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class DateUtil {
    static final long SECOND_MILLIS = 1000
    static final long MINUTE_MILLIS = SECOND_MILLIS * 60
    static final long HOUR_MILLIS = MINUTE_MILLIS * 60
    static final long DAY_MILLIS = HOUR_MILLIS * 24
    //static final long MONTH_MILLIS = DAY_MILLIS * 30
    //static final long YEAR_MILLIS = DAY_MILLIS * 365

    static Date extractDateOfBirth(String pid) {
        Date dateOfBirth = extractDateOfBirthFromString(pid, new SimpleDateFormat('yyyy-MM-dd'))
        if (!dateOfBirth) {
            dateOfBirth = extractDateOfBirthFromString(pid, new SimpleDateFormat('yyyyMMdd'))
        }
        if (!dateOfBirth && pid && pid?.size() > 9) {
            dateOfBirth = extractDateOfBirthFromString(pid, new SimpleDateFormat('yyMMdd'))
        }

        return dateOfBirth
    }

    private static Date extractDateOfBirthFromString(String pid, SimpleDateFormat sdf) {
        Date dateOfBirth = null
        try {
            if (pid.size() >= sdf.toPattern().size()) {
                String pidStart = pid.substring(0, sdf.toPattern().size())
                dateOfBirth = sdf.parse(pidStart)
                if (yearOf(dateOfBirth) > yearOf()) {
                    dateOfBirth = null
                }
            }
        }
        catch (Throwable ignore) {
        }

        return dateOfBirth
    }

    static Date dateFromTimeString(String inDate, Date regDate) {
        Date aDate = null

        if (inDate) {
            try {
                String dateString = removeTime()
                if (regDate) {
                    dateString = toYmdString(regDate)
                }
                String timeString = inDate
                if (timeString.length() == 4) {
                    timeString = inDate[0..1] + ":" + inDate[2..3]
                }
                timeString = timeString.replace('.', ':')
                timeString = timeString.replace(',', ':')
                aDate = fromStringWithTime(dateString, timeString)
            }
            catch (Throwable ignore) {
            }
        }

        return aDate

    }

    static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")
        dateFormat.setLenient(false)
        try {
            dateFormat.parse(inDate.trim())
        } catch (ParseException ignore) {
            return false
        }
        return true
    }

//=================  java.util date  ==================================

    // Current date
    static Date currentJavaUtilDate() {
        LocalDate localDate = LocalDate.now()
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    // Date from string YYYY-MM-DD
    static Date aDateFromYMD(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat('yyyy-MM-dd')
        Date date = sdf.parse(dateString)
        return removeTime(date)
    }

    // Keep only the date part
    static Date removeTime(date = new Date()) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    // A variant of toString for dates
    static String toYmdString(Date date = new Date()) {
        SimpleDateFormat sdf = new SimpleDateFormat('yyyy-MM-dd')
        return (date instanceof Date ? sdf.format(date) : '')
    }

    // A variant of toString for times
    static String toYmdHmsString(Date date = new Date()) {
        SimpleDateFormat sdf_time = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.SSS')
        if (!date) {
            return null
        }
        return sdf_time.format(date)
    }

    // Create a date from a string
    static Date fromString(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat('yyyy-MM-dd')
        return sdf.parse(dateString)
    }

    static Date fromStringWithTime(String dateString, String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat('yyyy-MM-dd HH:mm')
        return sdf.parse(dateString + " " + timeString)
    }

    // Difference in seconds
    static Integer diffSeconds(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) return 0
        return (Integer) ((laterDate.getTime() / SECOND_MILLIS) - (earlierDate.getTime() / SECOND_MILLIS))
    }

    // Difference in days
    static Integer diffDays(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) return 0
        return (Integer) ((Integer) (laterDate.getTime() / DAY_MILLIS) - (Integer) (earlierDate.getTime() / DAY_MILLIS))
    }

//=================  SQL date  ==================================

    // Current date
    static java.sql.Date currentDate() {
        def todayDate = new Date()
        Instant instant = todayDate.toInstant()
        instant = instant.truncatedTo(ChronoUnit.DAYS);
        todayDate = new java.sql.Date(Date.from(instant).time)
        return todayDate
    }

    static Integer yearOf(Date date = new Date()) {
        LocalDate localDate = new Date(date.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.getYear()
    }

    static Integer yearOf(java.sql.Date date) {
        LocalDate localDate = new Date(date.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.getYear()
    }

    static Integer monthOf(Date date) {
        LocalDate localDate = new Date(date.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.getMonthValue()
    }

    static Integer monthOf(java.sql.Date date) {
        LocalDate localDate = new Date(date.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.getMonthValue()
    }

    static Integer dayOf(java.sql.Date date) {
        LocalDate localDate = new Date(date.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.getDayOfMonth()
    }

    static Integer dayOf(Date date) {
        LocalDate localDate = new Date(date.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.getDayOfMonth()
    }

    // Difference in seconds
    static Integer diffSeconds(java.sql.Date earlierDate, java.sql.Date laterDate) {
        if (earlierDate == null || laterDate == null) return 0
        return (Integer) ((laterDate.getTime() / SECOND_MILLIS) - (earlierDate.getTime() / SECOND_MILLIS))
    }

    // Difference in minutes
    static Integer diffMinutes(java.sql.Date earlierDate, java.sql.Date laterDate) {
        if (earlierDate == null || laterDate == null) return 0
        return (Integer) ((laterDate.getTime() / MINUTE_MILLIS) - (earlierDate.getTime() / MINUTE_MILLIS))
    }

    // Difference in hours
    static Integer diffHours(java.sql.Date earlierDate, java.sql.Date laterDate) {
        if (earlierDate == null || laterDate == null) return 0
        return (Integer) ((laterDate.getTime() / HOUR_MILLIS) - (earlierDate.getTime() / HOUR_MILLIS))
    }

    // Difference in days
    static Integer diffDays(java.sql.Date earlierDate, java.sql.Date laterDate) {
        if (earlierDate == null || laterDate == null) return 0
        return (Integer) ((Integer) (laterDate.getTime() / DAY_MILLIS) - (Integer) (earlierDate.getTime() / DAY_MILLIS))
    }

}
