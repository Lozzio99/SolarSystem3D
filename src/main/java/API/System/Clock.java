package API.System;

import org.jetbrains.annotations.Contract;

import static API.Config.ERROR_EVALUATION;
import static API.Config.ERROR_MONTH_INDEX;

/**
 * Clock class used for testing and organising data
 */
public class Clock {
    public static final double DAY = 86400.0;
    public static final double HOUR = 3600;
    private static final String[] monthStrings =
            {"January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"};
    private int sec, min, hour, days, months, years;
    private boolean leap;
    private int[] daysInMonths;

    /**
     * Instantiates a new Clock.
     */
    public Clock() {
        sec = 0;
        min = 0;
        hour = 0;
        days = 0;
        months = 0;
        years = 0;
    }


    /**
     * Sets launch day.
     *
     * @return the launch day
     */
    public Clock setLaunchDay() {
        setInitialDay(1, 4, 2020);
        setInitialTime(0, 0, 0);
        checkLeap();
        if (ERROR_EVALUATION) checkFirst();
        return this;
    }

    /**
     * Sets initial day.
     *
     * @param days  the days
     * @param month the month
     * @param years the years
     * @return the initial day
     */
    public Clock setInitialDay(int days, int month, int years) {
        this.days = days;
        this.months = month;
        this.years = years;
        return this;
    }

    /**
     * Sets initial time.
     *
     * @param sec   the sec
     * @param min   the min
     * @param hours the hours
     * @return the initial time
     */
    public Clock setInitialTime(int sec, int min, int hours) {
        this.sec = sec;
        this.min = min;
        this.hour = hours;
        return this;
    }

    /**
     * Sets initial day.
     *
     * @param sec    the sec
     * @param min    the min
     * @param hour   the hours
     * @param days   the days
     * @param months the month
     * @param years  the years
     * @return the initial day
     */
    public Clock setInitialDayAndTime(int sec, int min, int hour, int days, int months, int years) {
        this.sec = sec;
        this.min = min;
        this.hour = hour;
        this.days = days;
        this.months = months;
        this.years = years;
        return this;
    }

    /**
     * Step boolean.
     *
     * @param secStep the sec step
     * @return the boolean
     */
    public synchronized boolean step(double secStep) {
        int step = (int) secStep;
        if (step != 0)
            secStep(step);
        checkLeap();
        return checkFirst();
    }

    private synchronized boolean checkFirst() {
        if (this.days == 1 && this.hour == 0 && this.min == 0 && this.sec == 0) {
            ERROR_MONTH_INDEX++;
            return true;
        }
        return false;
    }

    private synchronized void secStep(int secStep) {
        int minutes = secStep / 60;
        this.sec += secStep % 60;
        if (this.sec >= 60) {
            minutes += this.sec / 60;
            this.sec = this.sec % 60;
        }
        if (minutes != 0)
            minStep(minutes);
    }

    private synchronized void minStep(int minutes) {
        int hours = minutes / 60;
        this.min += minutes % 60;
        if (this.min >= 60) {
            hours += this.min / 60;
            this.min = this.min % 60;
        }
        if (hours != 0)
            hoursStep(hours);
    }

    private synchronized void hoursStep(int hours) {
        int days = hours / 24;
        this.hour += hours % 24;
        if (this.hour >= 24) {
            days += this.hour / 24;
            this.hour = this.hour % 24;
        }
        if (days != 0)
            daysStep(days);
    }

    private synchronized void daysStep(int days) {
        int months = days / this.daysInMonths[this.months - 1];

        //FIXME : problem here i guess,
        //TODO : receive 365, return days == days ( if ! leap )
        if (days >= 365)
            this.days += (days % 365) % this.daysInMonths[this.months - 1];
        else
            this.days += days % this.daysInMonths[this.months - 1];


        if (this.days > this.daysInMonths[this.months - 1]) {
            months += this.days / this.daysInMonths[this.months - 1];
            this.days = (this.days % this.daysInMonths[this.months - 1]);
            this.days = this.days == 0 ? 1 : this.days;
        }
        if (months != 0)
            monthsStep(months);
    }

    private synchronized void monthsStep(int months) {
        int years = months / 12;
        this.months += months % 12;
        if (this.months > 12) {
            years += this.months / 12;
            this.months = (this.months % 12);
            this.months = this.months == 0 ? 1 : this.months;
        }
        yearsStep(years);
    }

    private synchronized void yearsStep(int years) {
        this.years += years;
    }

    /**
     * Check leap.
     */
    public void checkLeap() {
        leap = this.years % 4 == 0;
        daysInMonths = new int[]{31, (leap ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    }

    @Override
    public synchronized String toString() {
        return "Clock { [ " +
                hour / 10 + hour % 10 + " : " +
                min / 10 + min % 10 + " : " +
                sec / 10 + sec % 10 + " ]     " +
                days / 10 + days % 10 + " / " +
                months / 10 + months % 10 + " / " +
                this.years + "  }";
    }

    /**
     * Gets sec.
     *
     * @return the sec
     */
    public int getSec() {
        return sec;
    }

    /**
     * Gets min.
     *
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Gets hour.
     *
     * @return the hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Gets days.
     *
     * @return the days
     */
    public int getDays() {
        return days;
    }

    /**
     * Gets months.
     *
     * @return the months
     */
    public int getMonths() {
        return months;
    }

    /**
     * Gets years.
     *
     * @return the years
     */
    public int getYears() {
        return years;
    }

    /**
     * Get days in months int [ ].
     *
     * @return the int [ ]
     */
    public int[] getDaysInMonths() {
        return daysInMonths;
    }

    /**
     * Month string string.
     *
     * @return the string
     */
    public String monthString() {
        return monthStrings[months - 1];
    }

    /**
     * Month string string.
     *
     * @return the string
     */
    public String monthString(int month) {
        if (month < 0 || month >= monthStrings.length) throw new IllegalArgumentException();
        return monthStrings[month];
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public final Date getDate() {
        final Date d = new Date();
        d.sec = this.sec;
        d.min = this.min;
        d.hours = this.hour;
        d.days = this.days;
        d.months = this.months;
        d.years = this.years;
        return d;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(sec);
        hash = 31 * hash + Integer.hashCode(min);
        hash = 31 * hash + Integer.hashCode(hour);
        hash = 31 * hash + Integer.hashCode(days);
        hash = 31 * hash + Integer.hashCode(months);
        hash = 31 * hash + Integer.hashCode(years);
        return hash;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Clock o) {
            return sec == o.sec && min == o.min &&
                    hour == o.hour && days == o.days &&
                    months == o.months && years == o.years;
        }
        return false;
    }

    /**
     * The type Date.
     */
    public static class Date {
        /**
         * The Sec.
         */
        public double sec;
        /**
         * The Min.
         */
        public double min;
        /**
         * The Hours.
         */
        public double hours;
        /**
         * The Days.
         */
        public double days;
        /**
         * The Months.
         */
        public double months;
        /**
         * The Years.
         */
        public double years;
    }
}
