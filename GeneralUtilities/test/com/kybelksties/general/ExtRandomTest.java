/*
 * Copyright (C) 2016 Dieter J Kybelksties
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
 * @date: 2016-01-07
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.general;

import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the public interface of the ExtRandom class.
 *
 * @author kybelksd
 */
public class ExtRandomTest
{

    private static final Class CLAZZ = ExtRandomTest.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    static java.util.Random javaRandom = new java.util.Random(0);

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
    public ExtRandomTest()
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
     * Test of nextInt method, of class ExtRandom.
     */
    @Test
    public void testNextInt()
    {
        ExtRandom instance = new ExtRandom(0);
        TestExpected<Integer>[] tests = new TestExpected[]
                        {
                            new TestExpected(null, null, 0, 1),
                            new TestExpected(0, null, 0, 1),
                            new TestExpected(0, 10, 0, 10),
                            new TestExpected(-10, 0, -10, 0),
                            new TestExpected(10, 0, 0, 10),
                            new TestExpected(0, -10, -10, 0),
                            new TestExpected(-100, 200, -100, 200),
                            new TestExpected(200, -100, -100, 200),
        };

        for (int i = 0; i < tests.length; i++)
        {
            TestExpected<Integer> test = tests[i];
            int result = instance.nextInt(test.low, test.high);
            String message = String.format("Test %d: %d  < %d failed",
                                           i,
                                           result,
                                           test.marginHigh);
            Assert.assertTrue(message, result < test.marginHigh);
            message = String.format("Test %d: %d  >= %d failed",
                                    i,
                                    result,
                                    test.marginLow);
            Assert.assertTrue(message, result >= test.marginLow);
        }
    }

    /**
     * Test of nextLong method, of class ExtRandom.
     */
    @Test
    public void testNextLong()
    {
        ExtRandom instance = new ExtRandom(0);
        Assert.assertEquals(0L, instance.nextLong(0L));
        Assert.assertTrue(5L > instance.nextLong(5L));
        TestExpected<Long>[] tests = new TestExpected[]
                     {
                         new TestExpected(null, null, 0L, 1L),
                         new TestExpected(0L, null, 0L, 1L),
                         new TestExpected(0L, 10L, 0L, 10L),
                         new TestExpected(-10L, 0L, -10L, 0L),
                         new TestExpected(10L, 0L, 0L, 10L),
                         new TestExpected(0L, -10L, -10L, 0L),
                         new TestExpected(-100L, 200L, -100L, 200L),
                         new TestExpected(200L, -100L, -100L, 200L),
        };

        for (int i = 0; i < tests.length; i++)
        {
            TestExpected<Long> test = tests[i];
            long result = instance.nextLong(test.low, test.high);
            String message = String.format("Test %d: %d  < %d failed",
                                           i,
                                           result,
                                           test.marginHigh);
            Assert.assertTrue(message, result < test.marginHigh);
            message = String.format("Test %d: %d  >= %d failed",
                                    i,
                                    result,
                                    test.marginLow);
            Assert.assertTrue(message, result >= test.marginLow);
        }
    }

    /**
     * Test of nextDouble method, of class ExtRandom.
     */
    @Test
    public void testNextDouble()
    {
        ExtRandom instance = new ExtRandom(0);
        Assert.assertEquals(0.0, instance.nextDouble(0.0), 0.0000000001);
        Assert.assertTrue(5L > instance.nextLong(5L));
        TestExpected<Double>[] tests = new TestExpected[]
                       {
                           new TestExpected(null, null, 0.0, 1.0),
                           new TestExpected(0.0, null, 0.0, 1.0),
                           new TestExpected(0.0, 10.0, 0.0, 10.0),
                           new TestExpected(-10.0, 0.0, -10.0, 0.0),
                           new TestExpected(10.0, 0.0, 0.0, 10.0),
                           new TestExpected(0.0, -10.0, -10.0, 0.0),
                           new TestExpected(-100.0, 200.0, -100.0, 200.0),
                           new TestExpected(200.0, -100.0, -100.0, 200.0),
        };

        for (int i = 0; i < tests.length; i++)
        {
            TestExpected<Double> test = tests[i];
            double result = instance.nextDouble(test.low, test.high);
            String message = String.format("Test %d: %f  < %f failed",
                                           i,
                                           result,
                                           test.marginHigh);
            Assert.assertTrue(message, result < test.marginHigh);
            message = String.format("Test %d: %f  >= %f failed",
                                    i,
                                    result,
                                    test.marginLow);
            Assert.assertTrue(message, result >= test.marginLow);
        }
    }

    /**
     * Test of nextFloat method, of class ExtRandom.
     */
    @Test
    public void testNextFloat()
    {
        ExtRandom instance = new ExtRandom(0);
        Assert.assertEquals(0.0F, instance.nextFloat(0.0F), 0.0000001);
        Assert.assertTrue(5L > instance.nextLong(5L));
        TestExpected<Float>[] tests = new TestExpected[]
                      {
                          new TestExpected(null, null, 0.0F, 1.0F),
                          new TestExpected(0.0F, null, 0.0F, 1.0F),
                          new TestExpected(0F, 10.0F, 0.0F, 10.0F),
                          new TestExpected(-10.0F, 0.0F, -10.0F, 0.0F),
                          new TestExpected(10.0F, 0.0F, 0.0F, 10.0F),
                          new TestExpected(0F, -10F, -10F, 0.0F),
                          new TestExpected(-100.0F, 200.0F, -100.0F, 200.0F),
                          new TestExpected(200.0F, -100.0F, -100.0F, 200.0F),
        };

        for (int i = 0; i < tests.length; i++)
        {
            TestExpected<Float> test = tests[i];
            float result = instance.nextFloat(test.low, test.high);
            String message = String.format("Test %d: %f  < %f failed",
                                           i,
                                           result,
                                           test.marginHigh);
            Assert.assertTrue(message, result < test.marginHigh);
            message = String.format("Test %d: %f  >= %f failed",
                                    i,
                                    result,
                                    test.marginLow);
            Assert.assertTrue(message, result >= test.marginLow);
        }
    }

    class TestExpected<T>
    {

        T low;
        T high;
        T marginLow;
        T marginHigh;

        TestExpected(T low, T high, T marginLow, T marginHigh)
        {
            this.low = low;
            this.high = high;
            this.marginLow = marginLow;
            this.marginHigh = marginHigh;
        }
    }
}
