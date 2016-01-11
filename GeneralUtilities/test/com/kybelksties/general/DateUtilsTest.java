
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

import static com.kybelksties.general.DateUtils.formatDetail;
import static com.kybelksties.general.DateUtils.fromString;
import static com.kybelksties.general.DateUtils.makeDate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the date utilities.
 *
 * @author kybelksd
 */
public class DateUtilsTest
{

    private static final String CLASS_NAME = DateUtilsTest.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Set up class statics.
     */
    @BeforeClass
    public static void setUpClass()
    {
    }

    /**
     * Tidy up class statics.
     */
    @AfterClass
    public static void tearDownClass()
    {
    }

    /**
     * Default construct.
     */
    public DateUtilsTest()
    {
    }

    /**
     * Set-up before any test.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Clean-up after any test.
     */
    @After
    public void tearDown()
    {
    }

    void checkFormat(HashSet<String> preDef, String fmt)
    {
        assertTrue("Format '" + fmt + "' not pre-defined", preDef.contains(fmt));
    }

    /**
     * Test of makeDateFormats method, of class DateUtils.
     */
    @Test
    public void testMakeDateFormats()
    {
        testMakeDateFormats(DateUtils.Preference.PREFER_EUROPEAN);
        testMakeDateFormats(DateUtils.Preference.PREFER_AMERICAN);
    }

    /**
     *
     * @param pref
     */
    public void testMakeDateFormats(DateUtils.Preference pref)
    {
        LOGGER.log(Level.INFO, "make predefined date formats ({0})", pref.
                   toString());
        if (pref.equals(DateUtils.Preference.PREFER_EUROPEAN))
        {
            DateUtils.initialiseFormatsEuropean();
        }
        else
        {
            DateUtils.initialiseFormatsAmerican();
        }

        ArrayList<String> preDefList = DateUtils.CommonDateFormats;
        HashSet<String> preDef = new HashSet<>();
        preDef.addAll(preDefList);
//        assertEquals("check format-list for duplicates",
//                     preDefList.size(),
//                     preDef.size());
        checkFormat(preDef, "yyyy-MMMM-dd HH:mm:ss");
        checkFormat(preDef, "yyyy-MMM-dd HH:mm:ss");
        checkFormat(preDef, "MMMM dd yyyy HH:mm:ss");
        checkFormat(preDef, "MMM dd yyyy HH:mm:ss");
        checkFormat(preDef, "dd MMMM yyyy HH:mm:ss");
        checkFormat(preDef, "dd MMM yyyy HH:mm:ss");
        checkFormat(preDef, "EEEE MMMM dd, yyyy HH:mm:ss");
        checkFormat(preDef, "EEEE MMM dd, yyyy HH:mm:ss");
        checkFormat(preDef, "EEEE dd MMMM, yyyy HH:mm:ss");
        checkFormat(preDef, "EEEE dd MMM, yyyy HH:mm:ss");
        checkFormat(preDef, "EEE MMMM dd, yyyy HH:mm:ss");
        checkFormat(preDef, "EEE MMM dd, yyyy HH:mm:ss");
        checkFormat(preDef, "EEE MMM dd HH:mm:ss z yyyy");
        checkFormat(preDef, "EEE dd MMMM, yyyy HH:mm:ss");
        checkFormat(preDef, "EEE dd MMM, yyyy HH:mm:ss");
        checkFormat(preDef, "MM/dd/yyyy HH:mm:ss");
        checkFormat(preDef, "dd/MM/yyyy HH:mm:ss");
        checkFormat(preDef, "yyyyMMdd_HHmmss");
        checkFormat(preDef, "HH:mm:ss");
        checkFormat(preDef, "HH:mm");
        checkFormat(preDef, "MMMM dd yyyy");
        checkFormat(preDef, "MMM dd yyyy");
        checkFormat(preDef, "dd MMMM yyyy");
        checkFormat(preDef, "dd MMM yyyy");
        checkFormat(preDef, "EEEE dd MMMM, yyyy");
        checkFormat(preDef, "EEEE dd MMM, yyyy");
        checkFormat(preDef, "EEE dd MMMM, yyyy");
        checkFormat(preDef, "EEE dd MMM, yyyy");
        checkFormat(preDef, "MMMM-dd-yyyy");
        checkFormat(preDef, "MMM-dd-yyyy");
        checkFormat(preDef, "dd-MMMM-yyyy");
        checkFormat(preDef, "dd-MMM-yyyy");
        checkFormat(preDef, "MM/dd/yyyy");
        checkFormat(preDef, "dd/MM/yyyy");
        checkFormat(preDef, "yyyyMMdd");
        checkFormat(preDef, "yyyyMMdd_HHmmss_SSS");
    }

    /**
     * Test of fromString method, of class DateUtils.
     */
    @Test
    public void testFromStringUsingExplicitFormat()
    {
        // make sure that we don't use any pre-defined formats
        DateUtils.resetFormats();

        LOGGER.log(Level.INFO, "parse strings to date using explicit format");
        try
        {
            Date parsed = DateUtils.fromString("", "");
            fail("Should have thrown ParseException but successfully parsed to '" +
                 parsed + "'");
        }
        catch (ParseException ex)
        {
            LOGGER.log(Level.INFO, "Caught expected ParseException: ", ex);
        }
        try
        {
            Date parsed = DateUtils.fromString("", "dd/MM/yyyy");
            fail("Should have thrown ParseException but successfully parsed to '" +
                 parsed + "'");
        }
        catch (ParseException ex)
        {
            LOGGER.log(Level.INFO, "Caught expected ParseException: ", ex);
        }
        try
        {
            Date parsed = DateUtils.fromString("10/11/1967", "");
            fail("Should have thrown ParseException but successfully parsed to '" +
                 parsed + "'");
        }
        catch (ParseException ex)
        {
            LOGGER.log(Level.INFO, "Caught expected ParseException: ", ex);
        }
        try
        {
            // European
            String expected =
                   formatDetail(makeDate(1967, Calendar.NOVEMBER, 10));

            Date parsed = DateUtils.fromString("10/11/1967", "dd/MM/yyyy");
            assertEquals(expected, formatDetail(parsed));
            parsed = DateUtils.fromString("Friday 10/11/1967", "EEE dd/MM/yyyy");
            assertEquals(expected, formatDetail(parsed));
            parsed = DateUtils.fromString("10/11/1967", "dd/MM/yyyy");
            assertEquals(expected, formatDetail(parsed));
            parsed = DateUtils.fromString("Friday 10 Nov, 1967",
                                          "EEEE dd MMM, yyyy");
            assertEquals(expected, formatDetail(parsed));

            expected = formatDetail(
            makeDate(1967, Calendar.NOVEMBER, 10, 12, 34, 56, 987));
            parsed = DateUtils.fromString("10/11/1967 12:34:56.987",
                                          "dd/MM/yyyy HH:mm:ss.SSS");
            assertEquals(expected, formatDetail(parsed));

            // American
            expected = formatDetail(makeDate(1967, Calendar.OCTOBER, 11));

            parsed = DateUtils.fromString("Fri 10/11/1967", "EEE MM/dd/yyyy");
            assertEquals(expected, formatDetail(parsed));
            parsed = DateUtils.fromString("Friday 10/11/1967", "EEE MM/dd/yyyy");
            assertEquals(expected, formatDetail(parsed));
            parsed = DateUtils.fromString("10/11/1967", "MM/dd/yyyy");
            assertEquals(expected, formatDetail(parsed));

            expected = formatDetail(
            makeDate(1967, Calendar.OCTOBER, 11, 12, 34, 56, 987));
            parsed = DateUtils.fromString("10/11/1967 12:34:56.987",
                                          "MM/dd/yyyy HH:mm:ss.SSS");
            assertEquals(expected, formatDetail(parsed));
        }
        catch (ParseException ex)
        {
            fail("Unexpected ParseException" + ex);
        }

    }

    /**
     * Test of fromString method, of class DateUtils.
     */
    @Test
    public void testFromStringPredefinedStockFormats()
    {
        LOGGER.log(Level.INFO, "parse strings to date using predefined formats");
        String[] testDates = new String[]
         {
             // Formats that include a time component
             "1967-November-10 12:34:56", // "yyyy-MMMM-dd HH:mm:ss"
             "1967-Nov-10 12:34:56", // "yyyy-MMM-dd HH:mm:ss"
             "November 10 1967 12:34:56", // "MMMM dd YYYY HH:mm:ss" American Format
             "Nov 10 1967 12:34:56", // "MMM dd YYYY HH:mm:ss" American Format
             "10 November 1967 12:34:56", // "dd MMMM yyyy HH:MM:ss"
             "10 Nov 1967 12:34:56", // "dd MMM yyyy HH:MM:ss"
             "Friday November 10, 1967 12:34:56", // "EEEE MMMM dd, yyyy HH:mm:ss" American Format
             "Friday Nov 10, 1967 12:34:56", // "EEEE MMM dd, yyyy HH:mm:ss" American Format
             "Friday 10 November, 1967 12:34:56", // "EEEE dd MMMM, yyyy HH:mm:ss"
             "Fri 10 Nov, 1967 12:34:56", // "EEEE dd MMM, yyyy HH:mm:ss"
             "Fri November 10, 1967 12:34:56", // "EEE MMMM dd, yyyy HH:mm:ss" American Format
             "Fri Nov 10, 1967 12:34:56", // "EEE MMM dd, yyyy HH:mm:ss" American Format
             "Fri Nov 10 12:34:56 GMT 1967", // "EEE MMM dd HH:mm:ss z yyyy" American Format
             "Fri 10 November, 1967 12:34:56", // "EEE dd MMMM, yyyy HH:mm:ss"
             "Friday 10 Nov, 1967 12:34:56", // "EEE dd MMM, yyyy HH:mm:ss"
             "10/28/1967 12:34:56", // "MM/dd/yyyy HH:mm:ss" American Format
             "28/11/1967 12:34:56", // "dd/MM/yyyy HH:mm:ss"
             "19671110_123456", // "yyyyMMdd_HHmmss"
             "12:34:56", // "HH:mm:ss" time only
             "12:34", // "HH:mm" time only

             // Formats that only have a date component
             "November 10 1967", // "MMMM dd yyyy" American Format
             "Nov 10 1967", // "MMM dd yyyy" American Format
             "10 November 1967", // "dd MMMM yyyy"
             "10 Nov 1967", // "dd MMM yyyy"
             "Friday 10 November, 1967", // "EEEE dd MMMM, yyyy"
             "Friday 10 Nov, 1967", // "EEEE dd MMM, yyyy"
             "Fri 10 November, 1967", // "EEE dd MMMM, yyyy"
             "Fri 10 Nov, 1967", // "EEE dd MMM, yyyy"
             "November-10-1967", // "MMMM-dd-yyyy" American Format
             "Nov-10-1967", // "MMM-dd-yyyy" American Format
             "10-November-1967", // "dd-MMMM-yyyy"
             "10-Nov-1967", // "dd-MMM-yyyy"
             "10/28/1967", // "MM/dd/yyyy" American Format
             "28/11/1967", // "dd/MM/yyyy"
             "19671110", // "yyyyMMdd"

             // test some with whitespace
             "  Fri   Nov\t 10  12:34:56 \t  GMT 1967 ", // "EEE MMM dd HH:mm:ss z yyyy" American Format
             "Fri  10   November,  \t 1967 12:34:56", // "EEE dd MMMM, yyyy HH:mm:ss"
             " Friday 10 Nov, \t1967 12:34:56 ", // "EEE dd MMM, yyyy HH:mm:ss"
             "\t\t10/28/1967 \t 12:34:56 ", // "MM/dd/yyyy HH:mm:ss" American Format
             "\t   28/11/1967\t   \t12:34:56 ", // "dd/MM/yyyy HH:mm:ss"
             " 19671110_123456   \t", // "yyyyMMdd_HHmmss"
             " 12:34:56", // "HH:mm:ss" time only
             "12:34 ", // "HH:mm" time only

             // Formats that only have a date component
             "November\t\t 10 1967", // "MMMM dd yyyy" American Format
             "Nov 10 1967\t\t", // "MMM dd yyyy" American Format
             "\t   \t10   November 1967 ", // "dd MMMM yyyy"
             " 10    Nov      1967    ", // "dd MMM yyyy"
             "\t\tFriday\t10\tNovember,\t1967\t", // "EEEE dd MMMM, yyyy"

        };
        DateUtils.initialiseFormatsEuropean();
        for (String dateString : testDates)
        {
            Date date = fromString(dateString);
            Assert.assertNotNull(
                    "European Format preferred:- Could not parse '" +
                    dateString +
                    "' ", date);
        }

        DateUtils.initialiseFormatsAmerican();
        for (String dateString : testDates)
        {
            Date date = fromString(dateString);
            Assert.assertNotNull(
                    "American Format preferred:- Could not parse '" +
                    dateString +
                    "' ", date);
        }

    }

    /**
     * Test of file_now method, of class DateUtils.
     */
    @Test
    public void testPreferEuropeanOrAmerican()
    {
        LOGGER.log(Level.INFO, "test prefer european or american");
        Date date;

        DateUtils.initialiseFormatsEuropean();
        date = fromString("10/11/1967 12:34:56");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertEquals(Calendar.NOVEMBER, cal.get(Calendar.MONTH));

        date = fromString("10/11/1967");
        cal.setTime(date);
        assertEquals(Calendar.NOVEMBER, cal.get(Calendar.MONTH));

        date = fromString("19671110_123456");
        cal.setTime(date);
        assertEquals(Calendar.NOVEMBER, cal.get(Calendar.MONTH));

        date = fromString("19671110");
        cal.setTime(date);
        assertEquals(Calendar.NOVEMBER, cal.get(Calendar.MONTH));

        //////////////////////////////////////////////
        DateUtils.initialiseFormatsAmerican();
        date = fromString("10/11/1967 12:34:56");
        cal.setTime(date);
        assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));

        date = fromString("10/11/1967");
        cal.setTime(date);
        assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));

        date = fromString("19671110_123456");
        cal.setTime(date);
        assertEquals(Calendar.NOVEMBER, cal.get(Calendar.MONTH));

        date = fromString("19671110");
        cal.setTime(date);
        assertEquals(Calendar.NOVEMBER, cal.get(Calendar.MONTH));
    }

}
