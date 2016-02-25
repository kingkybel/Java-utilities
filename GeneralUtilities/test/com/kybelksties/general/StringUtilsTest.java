
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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the public interface of the StringUtils class.
 *
 * @author kybelksd
 */
public class StringUtilsTest
{

    private static final String CLASS_NAME = StringUtilsTest.class.getName();
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
    public StringUtilsTest()
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

    /**
     * Test of findSpecies method, of class StringUtils.
     */
    @Test
    public void testStripWhitesopace()
    {
        LOGGER.log(Level.INFO, "replace multiple whitspace chars in string");
        class X
        {

            String orig;
            String result;

            X(String orig, String result)
            {
                this.orig = orig;
                this.result = result;
            }
        }

        X[] tests =
            new X[]
            {
                new X(null, null),
                new X("", ""),
                new X("a", "a"),
                new X("a s d f", "a s d f"),
                new X(" ", ""),
                new X(" a", "a"),
                new X(" a  s   d f ", "a s d f"),
                new X("\t", ""),
                new X("\ta", "a"),
                new X("\ta\t\ts\t\t\td\tf\t", "a s d f"),
                new X(" \t\t  ", ""),
                new X("\ta\t  \t", "a"),
                new X("  \ta \t\ts\t \t  \td \tf  \t", "a s d f"),
            };
        for (X test : tests)
        {
            assertEquals("stripping \"" + test.orig + "\"",
                         test.result,
                         StringUtils.trimWhitespace(test.orig));
        }
    }

    /**
     * Test of findSpecies method, of class StringUtils.
     */
    @Test
    public void testClassifyNumberString()
    {
        LOGGER.log(Level.INFO, "classify number string");
        class X
        {

            String s;
            StringUtils.NumberSpecies n;

            X(String s, StringUtils.NumberSpecies n)
            {
                this.s = s;
                this.n = n;
            }
        }

        X[] tests =
            new X[]
            {
                new X("", StringUtils.NumberSpecies.NONE),
                new X("true", StringUtils.NumberSpecies.NONE),
                new X("+0", StringUtils.NumberSpecies.INT),
                new X("-0", StringUtils.NumberSpecies.INT),
                new X("0", StringUtils.NumberSpecies.UINT),
                new X("1", StringUtils.NumberSpecies.UINT),
                new X("12", StringUtils.NumberSpecies.UINT),
                new X("-1", StringUtils.NumberSpecies.INT),
                new X("-12", StringUtils.NumberSpecies.INT),
                new X("+0.0", StringUtils.NumberSpecies.INT),
                new X("-0.0", StringUtils.NumberSpecies.INT),
                new X("0.0", StringUtils.NumberSpecies.UINT),
                new X("1.0", StringUtils.NumberSpecies.UINT),
                new X("12.0", StringUtils.NumberSpecies.UINT),
                new X("-1.0", StringUtils.NumberSpecies.INT),
                new X("-12.0", StringUtils.NumberSpecies.INT),
                new X("1.123", StringUtils.NumberSpecies.UFLOAT),
                new X("-1.123", StringUtils.NumberSpecies.FLOAT),
                new X("1e5", StringUtils.NumberSpecies.UFLOAT),
                new X("1f", StringUtils.NumberSpecies.UFLOAT),
            };

        for (X test : tests)
        {
            assertEquals("number class of \"" + test.s + "\"",
                         test.n,
                         StringUtils.findSpecies(test.s));
        }
        StringUtils.NumberSpecies nc =
                                StringUtils.commonSpecies(
                                        (String[]) null);

        assertEquals("number class of null-list",
                     StringUtils.NumberSpecies.NONE,
                     nc);
        nc = StringUtils.commonSpecies(new Object[0]);
        assertEquals("number class of empty list",
                     StringUtils.NumberSpecies.NONE,
                     nc);
        nc = StringUtils.commonSpecies(StringUtils.FALSE_VALUES);
        assertEquals("number class of false-values",
                     StringUtils.NumberSpecies.BOOL,
                     nc);
        nc = StringUtils.commonSpecies(StringUtils.TRUE_VALUES);
        assertEquals("number class of true-values",
                     StringUtils.NumberSpecies.BOOL,
                     nc);

        String[] uints = new String[]
         {
             "1", "2", "22", "50", "2000"
        };
        nc = StringUtils.commonSpecies(uints);
        assertEquals("number class of unsigned integer values",
                     StringUtils.NumberSpecies.UINT,
                     nc);

        String[] ints = new String[]
         {
             "-1", "2", "22", "50", "2000"
        };
        nc = StringUtils.commonSpecies(ints);
        assertEquals("number class of signed integer values",
                     StringUtils.NumberSpecies.INT,
                     nc);

        String[] ufloats = new String[]
         {
             "1.0", "2.01", "22", "50", "2000"
        };
        nc = StringUtils.commonSpecies(ufloats);
        assertEquals("number class of unsigned floating point values",
                     StringUtils.NumberSpecies.UFLOAT,
                     nc);

        String[] floats = new String[]
         {
             "1.0", "2.01", "22", "-50", "2000"
        };
        nc = StringUtils.commonSpecies(floats);
        assertEquals("number class of signed floating point values",
                     StringUtils.NumberSpecies.FLOAT,
                     nc);

        String[] chars = new String[]
         {
             "0", "1", "0", "a", "0"
        };
        nc = StringUtils.commonSpecies(chars);
        assertEquals("number class of character values",
                     StringUtils.NumberSpecies.CHAR,
                     nc);

        String[] strings = new String[]
         {
             "0", "1", "0", "abc", "0"
        };
        nc = StringUtils.commonSpecies(strings);
        assertEquals("number class of string values",
                     StringUtils.NumberSpecies.STRING,
                     nc);

    }

    /**
     * Test of scanBoolString method, of class StringUtils.
     */
    @Test
    public void testScanBoolString()
    {
        LOGGER.log(Level.INFO, "scan boolean strings");
        class X
        {

            String s;
            Boolean reval;

            X(String s, Boolean reval)
            {
                this.s = s;
                this.reval = reval;
            }
        }

        X[] tests =
            new X[]
            {
                new X(null, null),
                new X("", null),
                new X("dfsg", null),
                new X("T R U E", null),
                new X("t", true),
                new X("T", true),
                new X("true", true),
                new X("True", true),
                new X("tRue", true),
                new X("Y", true),
                new X("Yes", true),
                new X("YEAH", null),
                new X("1", true),
                new X("oN", true),
                new X("F...", null),
                new X("f", false),
                new X("F", false),
                new X("false", false),
                new X("False", false),
                new X("fALse", false),
                new X("N", false),
                new X("No", false),
                new X("NOPE", null),
                new X("0", false),
                new X("oFf", false),
            };

        for (X test : tests)
        {
            Boolean parsedValue = StringUtils.scanBoolString(test.s);
            String msg = "string \"" + test.s + "\" was parsed " +
                         (parsedValue != null ? "successfully" :
                          "unsuccessfully") +
                         " but should have " + (parsedValue != null ?
                                                "failed" :
                                                "succeeded") + ".";
            assertEquals(msg, test.reval, parsedValue);
            assertEquals("scan of \"" + test.s + "\" failed",
                         test.reval,
                         parsedValue);

        }
    }

    /**
     *
     */
    @Test
    public void testDump()
    {
        System.out.println(StringUtils.dump("asdf"));
//        System.out.println(StringUtils.dump(LOGGER));

    }
}
