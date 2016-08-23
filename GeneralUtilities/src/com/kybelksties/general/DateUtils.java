
/*
 * Copyright (C) 2015 Dieter J Kybelksties
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * @date: 2015-12-16
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.general;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A collection of utilities for date values,
 *
 * @author kybelksd
 */
public class DateUtils
{

    private static final Class CLAZZ = DateUtils.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Simple date-stamp that can be used as part of a filename.
     */
    public static final String FileDateFormatString = "yyyyMMdd";

    /**
     * Simple date-stamp that can be used as a log timestamp (sortable).
     */
    public static final String LogDateFormatString = "yyyyMMdd_HHmmss_SSS";

    /**
     * Date-format with the same String-ordering as date-ordering.
     */
    public static final String DateFormatString = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * Detail-date-format with the same String-ordering as date-ordering.
     */
    public static final String DetailDateFormatString =
                               "yyyy-MM-dd HH:mm:ss.SSS z (EEEE)";

    /**
     * Date and time formats are specified by date and time pattern strings.
     * (<em>quoted from Oracle JavaDocs</em>)<br>
     *
     * strings. Within date and time pattern strings, unquoted letters from
     * <code>'A'</code> to <code>'Z'</code> and from <code>'a'</code> to
     * <code>'z'</code> are interpreted as pattern letters representing the
     * components of a date or time string. Text can be quoted using single
     * quotes (<code>'</code>) to avoid interpretation. <code>"''"</code>
     * represents a single quote. All other characters are not interpreted;
     * they're simply copied into the output string during formatting or matched
     * against the input string during parsing.
     * <p>
     * The following pattern letters are defined (all other characters from
     * <code>'A'</code> to <code>'Z'</code> and from <code>'a'</code> to
     * <code>'z'</code> are reserved):
     * <blockquote>
     * <table border=0 cellspacing=3 cellpadding=0
     * summary="Chart shows pattern letters, date/time component, presentation, and examples.">
     * <tr bgcolor="#ccccff">
     * <th align=left>Letter
     * <th align=left>Date or Time Component
     * <th align=left>Presentation
     * <th align=left>Examples
     * <tr>
     * <td><code>G</code>
     * <td>Era designator
     * <td><a href="#text">Text</a>
     * <td><code>AD</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>y</code>
     * <td>Year
     * <td><a href="#year">Year</a>
     * <td><code>1996</code>; <code>96</code>
     * <tr>
     * <td><code>Y</code>
     * <td>Week year
     * <td><a href="#year">Year</a>
     * <td><code>2009</code>; <code>09</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>M</code>
     * <td>Month in year
     * <td><a href="#month">Month</a>
     * <td><code>July</code>; <code>Jul</code>; <code>07</code>
     * <tr>
     * <td><code>w</code>
     * <td>Week in year
     * <td><a href="#number">Number</a>
     * <td><code>27</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>W</code>
     * <td>Week in month
     * <td><a href="#number">Number</a>
     * <td><code>2</code>
     * <tr>
     * <td><code>D</code>
     * <td>Day in year
     * <td><a href="#number">Number</a>
     * <td><code>189</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>d</code>
     * <td>Day in month
     * <td><a href="#number">Number</a>
     * <td><code>10</code>
     * <tr>
     * <td><code>F</code>
     * <td>Day of week in month
     * <td><a href="#number">Number</a>
     * <td><code>2</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>E</code>
     * <td>Day name in week
     * <td><a href="#text">Text</a>
     * <td><code>Tuesday</code>; <code>Tue</code>
     * <tr>
     * <td><code>u</code>
     * <td>Day number of week (1 = Monday, ..., 7 = Sunday)
     * <td><a href="#number">Number</a>
     * <td><code>1</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>a</code>
     * <td>Am/pm marker
     * <td><a href="#text">Text</a>
     * <td><code>PM</code>
     * <tr>
     * <td><code>H</code>
     * <td>Hour in day (0-23)
     * <td><a href="#number">Number</a>
     * <td><code>0</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>k</code>
     * <td>Hour in day (1-24)
     * <td><a href="#number">Number</a>
     * <td><code>24</code>
     * <tr>
     * <td><code>K</code>
     * <td>Hour in am/pm (0-11)
     * <td><a href="#number">Number</a>
     * <td><code>0</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>h</code>
     * <td>Hour in am/pm (1-12)
     * <td><a href="#number">Number</a>
     * <td><code>12</code>
     * <tr>
     * <td><code>m</code>
     * <td>Minute in hour
     * <td><a href="#number">Number</a>
     * <td><code>30</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>s</code>
     * <td>Second in minute
     * <td><a href="#number">Number</a>
     * <td><code>55</code>
     * <tr>
     * <td><code>S</code>
     * <td>Millisecond
     * <td><a href="#number">Number</a>
     * <td><code>978</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>z</code>
     * <td>Time zone
     * <td><a href="#timezone">General time zone</a>
     * <td><code>Pacific Standard Time</code>; <code>PST</code>;
     * <code>GMT-08:00</code>
     * <tr>
     * <td><code>Z</code>
     * <td>Time zone
     * <td><a href="#rfc822timezone">RFC 822 time zone</a>
     * <td><code>-0800</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>X</code>
     * <td>Time zone
     * <td><a href="#iso8601timezone">ISO 8601 time zone</a>
     * <td><code>-08</code>; <code>-0800</code>;  <code>-08:00</code>
     * </table>
     * </blockquote>
     */
    public static ArrayList<String> CommonDateFormats;

    /**
     * yyyyMMdd.
     */
    public static final SimpleDateFormat FileDateFormat =
                                         new SimpleDateFormat(
                                                 FileDateFormatString);

    /**
     * yyyyMMdd_HHmmss_SSS.
     */
    public static final SimpleDateFormat LogDateFormat =
                                         new SimpleDateFormat(
                                                 LogDateFormatString);

    /**
     * yyyy-MM-dd HH:mm:ss.SSS.
     */
    public static final SimpleDateFormat DateFormat =
                                         new SimpleDateFormat(DateFormatString);

    /**
     * yyyy-MM-dd HH:mm:ss.SSS z (EEEE).
     */
    public static final SimpleDateFormat DetailDateFormat =
                                         new SimpleDateFormat(
                                                 DetailDateFormatString);

    static
    {
        initialiseFormatsEuropean();
    }

    /**
     * Initialize the set of common formats with the set of valid date formats
     * whilst giving preference to European formats where ambiguities with
     * American formats exist.
     */
    public static void initialiseFormatsEuropean()
    {
        CommonDateFormats = makeDateFormats(Preference.PREFER_EUROPEAN);
    }

    /**
     * Reset the set of common date formats (empty).
     */
    public static void resetFormats()
    {
        CommonDateFormats.clear();
    }

    /**
     * Initialize the set of common formats with the set of valid date formats
     * whilst giving preference to American formats where ambiguities with
     * European formats exist.
     */
    public static void initialiseFormatsAmerican()
    {
        CommonDateFormats = makeDateFormats(Preference.PREFER_AMERICAN);
    }

    /**
     * Create a list of formats to check date-strings against.
     *
     * @param preference if in doubt, prefer American or European
     * @return a list of formats
     */
    public static ArrayList<String> makeDateFormats(Preference preference)
    {
        ArrayList<String> reval = new ArrayList<>();
        String[] weekDayFmt = new String[]
         {
             "EEEE", "EEE", ""
        };
        String[] monthFmt = new String[]
         {
             "MMMM", "MMM", "MM"
        };
        String[] yearFmt = new String[]
         {
             "yyyy", "yy"
        };
        String[] timeFmt = new String[]
         {
             "HH:mm:ss.SSS", "HH:mm:ss", "HH:mm", "HH:mm:ss.SSS z",
             "HH:mm:ss z ", "HH:mm z", ""
        };
        String[] dayMonthYearFmt = new String[]
         {
             "%s-%s-%s", "%s/%s/%s", "%s %s %s", "%s %s, %s"
        };

        ArrayList<String> dateFmt = new ArrayList<>();
        for (String rawFmt : dayMonthYearFmt)
        {
            for (String month : monthFmt)
            {
                for (String year : yearFmt)
                {
                    if (preference == Preference.PREFER_EUROPEAN)
                    {
                        dateFmt.add(String.format(rawFmt, "dd", month, year));
                        dateFmt.add(String.format(rawFmt, month, "dd", year));
                    }
                    else
                    {
                        dateFmt.add(String.format(rawFmt, month, "dd", year));
                        dateFmt.add(String.format(rawFmt, "dd", month, year));
                    }
                    if (!"%s %s, %s".equals(rawFmt))
                    {
                        dateFmt.add(String.format(rawFmt, year, month, "dd"));
                    }
                }
            }
        }
        for (String month : monthFmt)
        {
            dateFmt.add(String.format("dd %s", month));
        }

        reval.add("EEE MMM dd HH:mm:ss z yyyy");
        for (String date : dateFmt)
        {
            for (String time : timeFmt)
            {
                for (String weekDay : weekDayFmt)
                {
                    String formatted = String.format("%s %s %s",
                                                     weekDay,
                                                     date,
                                                     time);
                    reval.add(StringUtils.trimWhitespace(formatted));
                    if (!time.isEmpty())
                    {
                        formatted = String.format("%s %s, %s",
                                                  weekDay,
                                                  date,
                                                  time);
                        reval.add(StringUtils.trimWhitespace(formatted));
                    }
                }

            }

        }

        for (String date : dateFmt)
        {
            reval.add(date);
        }

        reval.addAll(Arrays.asList(timeFmt));
        reval.add("yyyyMMdd");
        reval.add("yyyyMMdd_HHmmss");
        reval.add("yyyyMMdd_HHmmss_SSS");

        return reval;
    }

    /**
     * Create a date by trying defined formats in consecutive order.
     *
     * @param dateStr
     * @param format
     * @return the date created by parsing the string.
     * @throws java.text.ParseException
     */
    public static Date fromString(String dateStr, String format) throws
            ParseException
    {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.parse(dateStr);
    }

    /**
     * Create a date by trying defined formats in consecutive order.
     *
     * @param dateStr
     * @return the date created by parsing the string.
     */
    public static Date fromString(String dateStr)
    {
        Date reval = null;
        if (dateStr == null || dateStr.isEmpty())
        {
            return null;
        }
        String trimmedDateString = StringUtils.trimWhitespace(dateStr);
        boolean finished = false;
        for (int i = 0; i < CommonDateFormats.size() && !finished; i++)
        {
            finished = true;
            try
            {
                reval = fromString(trimmedDateString, CommonDateFormats.get(i));
            }
            catch (ParseException ex)
            {
                finished = false;
            }
        }
        return reval;
    }

    /**
     * Create a formatted date string that includes milliseconds.
     *
     * @param ms
     * @return the milliseconds converted to a string yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String formatMillis(long ms)
    {
        return DateFormat.format(new Date(ms));
    }

    /**
     * Create a formatted date string that includes milliseconds.
     *
     * @param date
     * @return the milliseconds converted to a string yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String formatMillis(Date date)
    {
        return DateFormat.format(date);
    }

    /**
     * Create a formatted date string that includes milliseconds.
     *
     * @param date
     * @return the milliseconds converted to a string yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String formatDetail(Date date)
    {
        return DetailDateFormat.format(date);
    }

    /**
     * Parse a formatted date-string into an epoch-number.
     *
     * @param dateString
     * @return the epoch-number if parseable, zero (0) otherwise
     */
    public static long toEpoch(String dateString)
    {
        long reval = 0L;
        if (!dateString.isEmpty())
        {
            try
            {
                Date dt = DateFormat.parse(dateString);
                reval = dt.getTime();
            }
            catch (ParseException ex)
            {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        return reval;
    }

    /**
     * Create a formatted date string from an epoch number.
     *
     * @param dateAsEpoch
     * @return the formatted string
     */
    public static String fromEpoch(Long dateAsEpoch)
    {
        Date dt = new Date(dateAsEpoch);
        String reval = DateFormat.format(dt);
        return reval;
    }

    /**
     * Get a timestamp as default date formatted String.
     *
     * @return the timestamp
     */
    public static String now()
    {
        Calendar cal = Calendar.getInstance();
        return DateFormat.format(cal.getTime());
    }

    /**
     * A sortable timestamp for the use in logs.
     *
     * @return a timestamp string in granularity order (most significant time
     *         element to least significant)
     */
    public static String logTimestamp()
    {
        Calendar cal = Calendar.getInstance();
        return LogDateFormat.format(cal.getTime());
    }

    /**
     * Get a timestamp as file date formatted String.
     *
     * @return the timestamp
     */
    public static String file_now()
    {
        Calendar cal = Calendar.getInstance();
        return FileDateFormat.format(cal.getTime());
    }

    /**
     * Create a date from integers.
     *
     * @param year  year as int value: if outside valid range, date will be
     *              invalid
     * @param month month as int value: [1..12]
     * @param day   day as int value: [1..28], [1..29],[1..30],[1..31] depending
     *              on month and leap-year status
     * @return the date
     */
    public static Date makeDate(int year, int month, int day)
    {
        return makeDate(year, month, day, 0, 0, 0, 0);
    }

    /**
     * Create a date from integers.
     *
     * @param year      year as int value: if outside valid range, date will be
     *                  invalid
     * @param month     month as int value: [1..12]
     * @param day       day as int value: [1..28], [1..29],[1..30],[1..31]
     *                  depending on month and leap-year status
     * @param hourOfDay hour as int [0..23]
     * @param minute    minute as int [0..59]
     * @return the date
     */
    public static Date makeDate(int year, int month, int day, int hourOfDay,
                                int minute)
    {
        return makeDate(year, month, day, hourOfDay, minute, 0, 0);
    }

    /**
     * Create a date from integers.
     *
     * @param year      year as int value: if outside valid range, date will be
     *                  invalid
     * @param month     month as int value: [1..12]
     * @param day       day as int value: [1..28], [1..29],[1..30],[1..31]
     *                  depending on month and leap-year status
     * @param hourOfDay hour as int [0..23]
     * @param minute    minute as int [0..59]
     * @param second    seconds as int [0..59]
     * @return the date
     */
    public static Date makeDate(int year,
                                int month,
                                int day,
                                int hourOfDay,
                                int minute,
                                int second)
    {
        return makeDate(year, month, day, hourOfDay, minute, 0, 0);
    }

    /**
     * Create a date from integers.
     *
     * @param year      year as int value: if outside valid range, date will be
     *                  invalid
     * @param month     month as int value: [1..12]
     * @param day       day as int value: [1..28], [1..29],[1..30],[1..31]
     *                  depending on month and leap-year status
     * @param hourOfDay hour as int [0..23]
     * @param minute    minute as int [0..59]
     * @param second    seconds as int [0..59]
     * @param millis    millisecond as int [0..999]
     * @return the date
     */
    public static Date makeDate(int year,
                                int month,
                                int day,
                                int hourOfDay,
                                int minute,
                                int second,
                                int millis)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hourOfDay, minute, second);
        cal.set(Calendar.MILLISECOND, millis);
        return cal.getTime();
    }

    /**
     * Flag for conflict resolution between American and European date formats.
     * With this flag ambiguities in time-strings between European and American
     * format will be resolved.<p>
     *
     * "10/11/1967" can be interpreted as 10th November 1967 in Europe or as
     * 11th October in America. If PREFER_EUROPEAN is set then the first
     * interpretation is used. otherwise the latter.
     */
    public static enum Preference
    {

        /**
         * Favour European formats.
         */
        PREFER_EUROPEAN,
        /**
         * Favour American formats.
         */
        PREFER_AMERICAN
    }
}
