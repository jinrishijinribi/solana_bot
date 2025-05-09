package com.rich.sol_bot.system.common;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimestampUtil {
    public static final Comparator<Timestamp> TIMESTAMP_COMPARATOR_INSTANCE = new TimestampComparator();

    public static class Constants {
        private Constants() {
        }

        public static final DateTimeFormatter DATA_TIME_FORMAT_COMPACT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        public static final DateTimeFormatter DATA_TIME_FORMAT_COMPACT_MILLIS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        public static final DateTimeFormatter DATA_FORMAT_COMPACT = DateTimeFormatter.ofPattern("yyyyMMdd");
        public static final DateTimeFormatter DATA_FORMAT_COMPACT_WITH_SPLIT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        public static final DateTimeFormatter NORMAL_DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public static final DateTimeFormatter NORMAL_DATE_TIME_WITH_ZONE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        public static final DateTimeFormatter NORMAL_DATE_TIME_WITH_ZONE_COMPACT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        public static final DateTimeFormatter NORMAL_DATE_TIME_MILLS_WITH_ZONE_COMPACT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        public static final DateTimeFormatter DATA_FORMAT_MINUTE_COMPACT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        public static final DateTimeFormatter UTC_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        public static long one_day_ms = 1000 * 60 * 60 * 24;
    }

    private TimestampUtil() {
    }

    public static long nowTs() {
//        return SystemClock.now();
        return System.currentTimeMillis();
    }

    public static class TimestampComparator implements Comparator<Timestamp> {
        @Override
        public int compare(Timestamp o1, Timestamp o2) {
            return o1.compareTo(o2);
        }
    }

    public static Timestamp now() {
        return new Timestamp(nowTs());
    }

    public static Timestamp zero() {
        return new Timestamp(0);
    }

    public static Timestamp minus(Timestamp from, long n, TimeUnit timeUnit) {
        return minus(from.getTime(), n, timeUnit);
    }

    public static Timestamp minus(long fromTs, long n, TimeUnit timeUnit) {
        final long difference = TimeUnit.MILLISECONDS.convert(n, timeUnit);
        long targetTimestamp = fromTs - difference;
        return new Timestamp(targetTimestamp <= 0 ? 0 : targetTimestamp);
    }

    public static Timestamp minus(long n, TimeUnit timeUnit) {
        final long now = nowTs();
        final long difference = TimeUnit.MILLISECONDS.convert(n, timeUnit);
        long targetTimestamp = now - difference;
        return new Timestamp(targetTimestamp <= 0 ? 0 : targetTimestamp);
    }

    public static Timestamp plus(Duration duration) {
        return plus(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    public static Timestamp plus(Timestamp from, Duration duration) {
        return plus(from, duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    public static Timestamp plus(Timestamp from, long n, TimeUnit timeUnit) {
        return plus(from.getTime(), n, timeUnit);
    }

    public static Timestamp plus(long from, long n, TimeUnit timeUnit) {
        final long difference = TimeUnit.MILLISECONDS.convert(n, timeUnit);
        return new Timestamp(from + difference);
    }

    public static Timestamp plus(long n, TimeUnit timeUnit) {
        final long now = nowTs();
        final long difference = TimeUnit.MILLISECONDS.convert(n, timeUnit);
        return new Timestamp(now + difference);
    }

    public static Timestamp tomorrowBegin(ZoneId zoneId) {
        return fromInstant(ZonedDateTime.now(zoneId).plusDays(1).truncatedTo(ChronoUnit.DAYS).toInstant());
    }

    public static Timestamp fromLocalDateTime(LocalDateTime time) {
        return Timestamp.valueOf(time);
    }

    public static Timestamp fromDate(Date time) {
        return new Timestamp(time.getTime());
    }

    public static Timestamp fromTs(String ts) {
        if (StringUtils.isEmpty(ts)) {
            return null;
        }
        switch (ts.length()) {
            case 10:
                return new Timestamp(Long.parseLong(ts) * 1000);
            case 13:
                return new Timestamp(Long.parseLong(ts));
            default:
                throw new IllegalArgumentException("invalid timestamp number format:" + ts);
        }
    }

    public static Timestamp fromMillis(long millis) {
        return new Timestamp(millis);
    }

    public static Timestamp fromMillis(String millis) {
        return fromMillis(Long.parseLong(millis));
    }

    public static Timestamp fromSeconds(long seconds) {
        return new Timestamp(seconds * 1000);
    }

    public static Timestamp fromSeconds(String seconds) {
        return fromSeconds(Long.parseLong(seconds));
    }

    public static Timestamp fromInstant(Instant instant) {
        return new Timestamp(instant.toEpochMilli());
    }

    public static Range<Timestamp> rangeOf(Timestamp startTime, Timestamp endTime) {
        return Range.between(startTime, endTime, TIMESTAMP_COMPARATOR_INSTANCE);
    }

    public static Timestamp parse(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        final LocalDateTime localDateTime = LocalDateTime.parse(value, Constants.NORMAL_DATE_TIME_PATTERN);
        final Instant instant = ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant();
        return Timestamp.from(instant);
    }

    public static String utcFormat(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
        return zonedDateTime.format(Constants.UTC_DATE_FORMAT);
    }

    public static String localFormat(Timestamp timestamp) {
        return localFormat(timestamp, Constants.NORMAL_DATE_TIME_PATTERN);
    }

    public static String localFormat(Timestamp timestamp, DateTimeFormatter pattern) {
        if (timestamp == null) {
            return null;
        }
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.format(pattern);
    }

    public static String formatWithZone(Timestamp timestamp, DateTimeFormatter pattern, ZoneId zone) {
        if (timestamp == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = timestamp.toInstant().atZone(zone);
        return zonedDateTime.format(pattern);
    }

    public static String formatWithZone(Timestamp timestamp, ZoneId zone) {
        return formatWithZone(timestamp, Constants.NORMAL_DATE_TIME_WITH_ZONE, zone);
    }

    public static Timestamp getZeroTimestamp(Timestamp now, Integer offset, Duration duration) {
        // 整点校正
        long days = Math.floorDiv(now.getTime(), Constants.one_day_ms);
        // 0时区0时的ts
        Timestamp zeroTs = new Timestamp(days * Constants.one_day_ms);
        // 8时区0时的ts
        Timestamp offsetTs = TimestampUtil.plus(zeroTs, Duration.ofHours(-offset));
        // 8时区 和 0点偏差的 ts
        Timestamp actStartTs = TimestampUtil.plus(offsetTs, duration);
        while (now.getTime() < actStartTs.getTime()) {
            actStartTs = new Timestamp(actStartTs.getTime() - Constants.one_day_ms);
        }
        while (now.getTime() > actStartTs.getTime() + Constants.one_day_ms) {
            actStartTs = new Timestamp(actStartTs.getTime() + Constants.one_day_ms);
        }
        return actStartTs;
    }

    public static Timestamp todayBegin() {
        return todayBegin(Calendar.getInstance());
    }

    public static Timestamp todayBegin(Calendar c) {
        Calendar r = Calendar.getInstance();
        r.setTime(c.getTime());
        setToDayBegin(r);
        return fromMillis(r.getTimeInMillis());
    }

    public static Timestamp todayEnd() {
        return todayEnd(Calendar.getInstance());
    }

    public static Timestamp todayEnd(Calendar c) {
        Calendar r = Calendar.getInstance();
        r.setTime(c.getTime());
        setToDayEnd(r);
        return fromMillis(r.getTimeInMillis());
    }

    public static Timestamp yesterdayBegin() {
        return yesterdayBegin(Calendar.getInstance());
    }

    public static Timestamp yesterdayBegin(Calendar c) {
        Calendar r = Calendar.getInstance();
        r.setTime(c.getTime());
        r.add(Calendar.DAY_OF_YEAR, -1);
        setToDayBegin(r);
        return fromMillis(r.getTimeInMillis());
    }

    public static Timestamp yesterdayEnd() {
        return yesterdayEnd(Calendar.getInstance());
    }

    public static Timestamp yesterdayEnd(Calendar c) {
        Calendar r = Calendar.getInstance();
        r.setTime(c.getTime());
        r.add(Calendar.DAY_OF_YEAR, -1);
        setToDayEnd(r);
        return fromMillis(r.getTimeInMillis());
    }

    public static void setToDayBegin(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    public static void setToDayEnd(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 0);
    }

    // 返回某个时区
    public static Timestamp getTimestampOfZone(Timestamp day, Long zoneOffset) {
        long i = (day.getTime() + zoneOffset * (60 * 60) * 1000) % (60 * 60 * 24 * 1000);
        return new Timestamp(day.getTime() - i);
    }

    // Wed Nov 13 05:15:07 +0000 2024
    public static Timestamp convertFormat(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss xxxx yyyy").localizedBy(Locale.ENGLISH);
        OffsetDateTime dateTime = OffsetDateTime.parse(str, formatter);
        long timestampInMilliseconds = dateTime.toInstant().toEpochMilli();
        return new Timestamp(timestampInMilliseconds);
    }
}
