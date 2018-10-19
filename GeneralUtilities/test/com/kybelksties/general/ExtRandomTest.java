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

import com.kybelksties.general.ExtRandom.DistinctValues;
import java.util.HashMap;
import java.util.HashSet;
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
            int result = instance.nextInt(test.paramLow, test.paramHigh);
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
            long result = instance.nextLong(test.paramLow, test.paramHigh);
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
            double result = instance.nextDouble(test.paramLow, test.paramHigh);
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
            float result = instance.nextFloat(test.paramLow, test.paramHigh);
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
     * Test of nextEnum method, of class ExtRandom.
     */
    @Test
    public void testNextEnum()
    {
        ExtRandom instance = new ExtRandom(0);
        TestExpected<SampleEnum>[] tests = new TestExpected[]
                           {
                               new TestExpected(null,
                                                SampleEnum.V14,
                                                SampleEnum.V01,
                                                SampleEnum.V14),
                               new TestExpected(SampleEnum.V01,
                                                null,
                                                SampleEnum.V01,
                                                SampleEnum.V14),
                               new TestExpected(SampleEnum.V05,
                                                SampleEnum.V10,
                                                SampleEnum.V05,
                                                SampleEnum.V10),
                               new TestExpected(SampleEnum.V10,
                                                SampleEnum.V05,
                                                SampleEnum.V05,
                                                SampleEnum.V10)

        };
        try
        {
            for (int n = 0; n < 10; n++)
            {
                for (int i = 0; i < tests.length; i++)
                {
                    TestExpected<SampleEnum> test = tests[i];
                    SampleEnum result = instance.nextEnum(test.paramLow,
                                                          test.paramHigh);
                    String message = String.
                           format("Test %d.%d: %s  < %s failed",
                                  n,
                                  i,
                                  result,
                                  test.marginHigh);
                    Assert.assertTrue(message, result.ordinal() <
                                               test.marginHigh.ordinal());
                    message = String.format("Test %d.%d: %s  >= %s failed",
                                            n,
                                            i,
                                            result,
                                            test.marginLow);
                    Assert.assertTrue(message,
                                      result.ordinal() >=
                                      test.marginLow.ordinal());
                }
            }

        }
        catch (Exception e)
        {
            Assert.fail("Unexpected exception :" + e.toString());
        }
    }

    /**
     * Test of nextEnum method, of class ExtRandom.
     */
    @Test
    public void testNextEnum_1_arg()
    {
        ExtRandom instance = new ExtRandom(0);
        TestExpected<SampleEnum>[] tests = new TestExpected[]
                           {
                               new TestExpected(null,
                                                SampleEnum.V08,
                                                SampleEnum.V01,
                                                SampleEnum.V08),
                               new TestExpected(SampleEnum.V05,
                                                SampleEnum.V10,
                                                SampleEnum.V01,
                                                SampleEnum.V10),
                               new TestExpected(SampleEnum.V10,
                                                SampleEnum.V05,
                                                SampleEnum.V01,
                                                SampleEnum.V05)

        };
        try
        {
            for (int n = 0; n < 10; n++)
            {
                for (int i = 0; i < tests.length; i++)
                {
                    TestExpected<SampleEnum> test = tests[i];
                    SampleEnum result = instance.nextEnum(test.paramHigh);
                    String message = String.
                           format("Test %d.%d: %s  < %s failed",
                                  n,
                                  i,
                                  result,
                                  test.marginHigh);
                    Assert.assertTrue(message, result.ordinal() <
                                               test.marginHigh.ordinal());
                    message = String.format("Test %d.%d: %s  >= %s failed",
                                            n,
                                            i,
                                            result,
                                            test.marginLow);
                    Assert.assertTrue(message,
                                      result.ordinal() >= test.marginLow.
                                      ordinal());
                }
            }

        }
        catch (Exception e)
        {
            Assert.fail("Unexpected exception :" + e.toString());
        }
    }

    /**
     * Test of nextEnum method, of class ExtRandom.
     */
    @Test
    public void testDistinctValues()
    {
        ExtRandom instance = new ExtRandom(0);
        final Object[][] valueProb =
                         new Object[][]
                         {
                             new Object[]
                             {
                                 "Abc", 0.5
                             },
                             new Object[]
                             {
                                 5, 0.1
                             },
                             new Object[]
                             {
                                 7.0, 0.3
                             },
                             new Object[]
                             {
                                 "xxx", 0.1
                             }

                         };
        ExtRandom.DistinctValues dv = instance.new DistinctValues(valueProb);

        HashMap<Object, Double> bucket = new HashMap<>();
        double numberTests = 54327.0;
        for (double i = 0; i < numberTests; i++)
        {
            Object o = dv.next();
            Double current = bucket.containsKey(o) ? bucket.get(o) : 0.0;
            bucket.put(o, current + 1.0 / numberTests);
        }
        Assert.assertTrue(bucket.get("Abc") > 0.4 && bucket.get("Abc") < 0.6);
        Assert.assertTrue("Frequency of '5'=" + bucket.get(5),
                          bucket.get(5) > 0.05 && bucket.get(5) < 0.15);
        Assert.assertTrue(bucket.get(7.0) > 0.25 && bucket.get(7.0) < 0.35);
        Assert.assertTrue(bucket.get("xxx") > 0.05 && bucket.get("xxx") < 0.15);

        ///////
        bucket = new HashMap<>();
        HashSet<Object> vals = new HashSet<>();
        vals.add(1);
        vals.add("B");
        vals.add(5L);
        vals.add(7.8);
        dv.setData(vals);
        for (double i = 0; i < numberTests; i++)
        {
            Object o = dv.next();
            Double current = bucket.containsKey(o) ? bucket.get(o) : 0.0;
            bucket.put(o, current + 1.0 / numberTests);
        }
        Assert.assertTrue(bucket.get(1) > 0.2 && bucket.get(1) < 0.3);
        Assert.assertTrue(bucket.get("B") > 0.2 && bucket.get("B") < 0.3);
        Assert.assertTrue(bucket.get(5L) > 0.2 && bucket.get(5L) < 0.3);
        Assert.assertTrue(bucket.get(7.8) > 0.2 && bucket.get(7.8) < 0.3);
    }

    enum SampleEnum
    {

        V01, V02, V03, V04, V05, V06, V07, V08, V09, V10, V11, V12, V13, V14
    }

    class TestExpected<T>
    {

        T paramLow;
        T paramHigh;
        T marginLow;
        T marginHigh;

        TestExpected(T low, T high, T marginLow, T marginHigh)
        {
            this.paramLow = low;
            this.paramHigh = high;
            this.marginLow = marginLow;
            this.marginHigh = marginHigh;
        }
    }
}
