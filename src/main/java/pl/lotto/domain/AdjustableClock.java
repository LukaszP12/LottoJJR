package pl.lotto.domain;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class AdjustableClock extends Clock {

    private ZoneId zone;
    private Instant instant;// the field that actually keeps the time

    public AdjustableClock(Instant initialInstant, ZoneId zone) {
        this.instant = initialInstant;
        this.zone = zone;
    }

    public static AdjustableClock ofLocalDateAndLocalTime(LocalDate date, LocalTime time, ZoneId zone) {
        Instant fixedInstant = date.atTime(time).atZone(zone).toInstant();
        return new AdjustableClock(fixedInstant, zone);
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        if (zone.equals(this.zone)) {
            return this;
        }
        return new AdjustableClock(this.instant, zone);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    public void advanceInTimeBy(Duration duration) {
        this.instant = this.instant.plus(duration);
    }

    public void plusDays(int days) {
        advanceInTimeBy(Duration.ofDays(days));
    }

    public void plusMinutes(int minutes) {
        advanceInTimeBy(Duration.ofMinutes(minutes));
    }

    public void plusDaysAndMinutes(int days, int minutes) {
        plusDays(days);
        plusMinutes(minutes);
    }

    public void setClockToLocalDateTime(LocalDateTime localDateTime) {
        this.instant = localDateTime.atZone(zone).toInstant();
    }

    public void setClockToLocalDate(LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.now(this));
        setClockToLocalDateTime(localDateTime);
    }

    public void setClockToLocalTime(LocalTime localTime) {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(this), localTime);
        setClockToLocalDateTime(localDateTime);
    }

    public LocalDateTime now() {
        return LocalDateTime.ofInstant(this.instant(), this.getZone());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AdjustableClock)) return false;
        AdjustableClock other = (AdjustableClock) obj;
        return instant.equals(other.instant) && zone.equals(other.zone);
    }

    @Override
    public int hashCode() {
        return instant.hashCode() ^ zone.hashCode();
    }

    @Override
    public String toString() {
        return "AdjustableClock[" + instant + "," + zone + "]";
    }
}
