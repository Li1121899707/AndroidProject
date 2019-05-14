package com.example.yantu.androidproject.Util.timer;

/**
 * Time Counter object
 * Support thread safe time minus and
 *
 * @author Ramen (lxdlam@gmail.com)
 * @date 2019.04.26
 */
public class TimeCounter {
    /**
     * Time elements and the total seconds
     */
    private long hours, minutes, seconds;
    private long totalSeconds;

    /**
     * Calculate the remaining time
     */
    private synchronized void adjust() {
        long cur = totalSeconds;
        seconds = cur % 60;
        cur -= seconds;
        cur /= 60;
        minutes = cur % 60;
        hours = cur / 60;
    }

    /**
     * Default constructor
     */
    public TimeCounter() {
        hours = minutes = seconds = totalSeconds = 0;
    }

    /**
     * Copy constructor
     *
     * @param tc the object copied from
     */
    public TimeCounter(TimeCounter tc) {
        hours = tc.hours;
        minutes = tc.minutes;
        seconds = tc.seconds;
        totalSeconds = tc.totalSeconds;
    }

    /**
     * Detailed constructor
     *
     * @param hours   hours should be delayed
     * @param minutes minutes should be delayed
     * @param seconds seconds should be delayed
     */
    public TimeCounter(long hours, long minutes, long seconds) throws IllegalArgumentException {
        if (minutes < 0 || minutes > 59)
            throw new IllegalArgumentException("minutes must be larger than 0 and less than 60");
        if (hours < 0) throw new IllegalArgumentException("hours must be larger than 0");
        if (seconds < 0 || seconds > 59)
            throw new IllegalArgumentException("seconds must be larger than 0 and less than 60");

        setHours(hours);
        setMinutes(minutes);
        setSeconds(seconds);
        setTotalSeconds(3600L * hours + 60 * minutes + seconds);
    }

    /**
     * Consume one second.
     *
     * @return if it reach the goal.
     */
    public synchronized boolean consume() {
        if (totalSeconds <= 1) return false;
        totalSeconds--;
        adjust();
        return true;
    }

    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public void setTotalSeconds(long totalSeconds) {
        this.totalSeconds = totalSeconds;
    }
}

