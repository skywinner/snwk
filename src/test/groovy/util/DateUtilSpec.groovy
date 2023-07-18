package util

import groovy.time.TimeCategory
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class DateUtilSpec extends Specification {

    void "current date is returned"() {
        given: "today"
        Date today = new Date()

        when: "current date is fetched"
        Date utilDate = DateUtil.currentJavaUtilDate()

        then: "the dates match"
        int year = DateUtil.yearOf(today)
        int month = DateUtil.monthOf(today)
        int day = DateUtil.dayOf(today)

        LocalDate localUtilDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        int yearUtilDate = localUtilDate.getYear()
        int monthUtilDate = localUtilDate.getMonthValue()
        int dayUtilDate = localUtilDate.getDayOfMonth()

        year == yearUtilDate
        month == monthUtilDate
        day == dayUtilDate
    }

    void "time difference in seconds is returned for sql date"() {
        use(TimeCategory) {
            given: "today"
            java.sql.Date fromDate = DateUtil.currentDate()

            and: "tomorrow"
            Instant instant = fromDate.plus(1).toInstant()
            instant = instant.truncatedTo(ChronoUnit.DAYS);
            java.sql.Date toDate = new java.sql.Date(Date.from(instant).time)

            when: "the difference is calculated"
            Integer diff = DateUtil.diffSeconds(fromDate, toDate)

            then: "the diff is correct"
            diff == 60 * 60 * 24
        }
    }

    void "time difference in minutes is returned for sql date"() {
        use(TimeCategory) {
            given: "today"
            java.sql.Date fromDate = DateUtil.currentDate()

            and: "tomorrow"
            Instant instant = fromDate.plus(1).toInstant()
            instant = instant.truncatedTo(ChronoUnit.DAYS);
            java.sql.Date toDate = new java.sql.Date(Date.from(instant).time)

            when: "the difference is calculated"
            Integer diff = DateUtil.diffMinutes(fromDate, toDate)

            then: "the diff is correct"
            diff == 60 * 24
        }
    }

    void "time difference in hours is returned for sql date"() {
        use(TimeCategory) {
            given: "today"
            java.sql.Date fromDate = DateUtil.currentDate()

            and: "tomorrow"
            Instant instant = fromDate.plus(1).toInstant()
            instant = instant.truncatedTo(ChronoUnit.DAYS);
            java.sql.Date toDate = new java.sql.Date(Date.from(instant).time)

            when: "the difference is calculated"
            Integer diff = DateUtil.diffHours(fromDate, toDate)

            then: "the diff is correct"
            diff == 24
        }
    }

    void "time difference in days is returned for sql date"() {
        use(TimeCategory) {
            given: "today"
            java.sql.Date fromDate = DateUtil.currentDate()

            and: "tomorrow and more"
            Instant instant = fromDate.plus(33).toInstant()
            instant = instant.truncatedTo(ChronoUnit.DAYS);
            java.sql.Date toDate = new java.sql.Date(Date.from(instant).time)

            when: "the difference is calculated"
            Integer diff = DateUtil.diffDays(fromDate, toDate)

            then: "the diff is correct"
            diff == 33
        }
    }

    void "time difference in days is returned"() {
        use(TimeCategory) {
            given: "today"
            Date fromDate = DateUtil.currentJavaUtilDate()

            and: "tomorrow and more"
            Instant instant = fromDate.plus(33).toInstant()
            instant = instant.truncatedTo(ChronoUnit.DAYS);
            Date toDate = new Date(Date.from(instant).time)

            when: "the difference is calculated"
            Integer diff = DateUtil.diffDays(fromDate, toDate)

            then: "the diff is correct"
            diff == 33
        }
    }

    void "year part of a date"() {
        given: "today"
        java.sql.Date fromDate = DateUtil.currentDate()

        when: "the year is fetched"
        def amount = DateUtil.yearOf(fromDate)

        then: "the year is correct"
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        def year = localDate.getYear()

        amount == year
    }

    void "month part of a date"() {
        given: "today"
        java.sql.Date fromDate = DateUtil.currentDate()

        when: "the month is fetched"
        def amount = DateUtil.monthOf(fromDate)

        then: "the year is correct"
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        def month = localDate.getMonthValue()

        amount == month
    }

    void "day part of a date"() {
        given: "today"
        java.sql.Date fromDate = DateUtil.currentDate()

        when: "the day is fetched"
        def amount = DateUtil.dayOf(fromDate)

        then: "the year is correct"
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        def day = localDate.getDayOfMonth()
        amount == day
    }

    void "validate string as date"() {
        given: "expectations of the result"
        def expected = []
        expected.add(['2004-02-29', true])
        expected.add(['2005-02-29', false])

        when: "date (string) is validated"
        boolean after = false
        expected.each { String given, boolean exp ->
            after = DateUtil.isValidDate(given)
            then: "the result comes out as expected"
            assert exp == after
        }

        then: 'done'
        expected
    }

    void "create a date from string"() {
        when: "date is created"
        Date aDate = DateUtil.aDateFromYMD('2020-12-31')

        then: 'done'
        aDate instanceof Date
    }

    void "remove the time part"() {
        SimpleDateFormat sdf_hour = new SimpleDateFormat('HH')
        SimpleDateFormat sdf_minute = new SimpleDateFormat('mm')
        SimpleDateFormat sdf_second = new SimpleDateFormat('ss')

        when: "date is created"
        Date aDate = DateUtil.removeTime()

        then: 'done'
        aDate instanceof Date
        sdf_hour.format(aDate) == "00"
        sdf_minute.format(aDate) == "00"
        sdf_second.format(aDate) == "00"
    }

    void "create a YMD-string from Date"() {
        SimpleDateFormat sdf = new SimpleDateFormat('yyyy-MM-dd')
        Date aDate = new Date()

        when: "String is created"
        String aDateString = DateUtil.toYmdString(aDate)

        then: 'done'
        aDateString == sdf.format(aDate)
    }

    void "create a YMDHms-string from Date"() {
        SimpleDateFormat sdf_time = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss.SSS')
        Date aDate = new Date()

        when: "String is created"
        String aDateString = DateUtil.toYmdHmsString(aDate)

        then: 'done'
        aDateString == sdf_time.format(aDate)
    }

    void "create a date from string - again"() {
        when: "date is created"
        Date aDate = DateUtil.fromString('2020-12-31')

        then: 'done'
        aDate instanceof Date
    }

    void "create a datetime from string"() {
        when: "date is created"
        Date aDate = DateUtil.fromStringWithTime('2020-12-31', '12:34')

        then: 'done'
        aDate instanceof Date
    }

    void "extract date from string"() {
        given: "expectations of the result"
        def expected = []
        expected.add([null, null])
        expected.add(['apa', null])
        expected.add(['999999', null])
        expected.add(['99999912', null])
        expected.add(['20990101-1212', null])
        expected.add(['20190101-1212', DateUtil.aDateFromYMD('2019-01-01')])
        expected.add(['196503301035', DateUtil.aDateFromYMD('1965-03-30')])
        expected.add(['19650330', DateUtil.aDateFromYMD('1965-03-30')])
        expected.add(['6503301035', DateUtil.aDateFromYMD('1965-03-30')])

        when: "date is extracted fro mthe strings"
        Date after = null
        expected.each { String pid, Date exp ->
            after = DateUtil.extractDateOfBirth(pid)
            then: "the result comes out as expected"
            if (exp != after) {
                println("date from string exp=${exp}  after=${after}")
            }
            assert exp == after
        }

        then: 'done'
        expected
    }

}
