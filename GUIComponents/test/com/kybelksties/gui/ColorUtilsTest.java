
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
package com.kybelksties.gui;

import java.awt.Color;
import java.awt.MultipleGradientPaint;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kybelksd
 */
public class ColorUtilsTest
{

    private static final Class CLAZZ = ColorUtilsTest.class;
    private static final String CLASS_NAME = CLAZZ.getName();
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
    public ColorUtilsTest()
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
     * Test of colorDifference method, of class ColorUtils.
     */
    @Test
    public void testColorDifference()
    {
        System.out.println("colorDifference");
        Color c1 = null;
        Color c2 = null;
        int result = ColorUtils.colorDifference(c1, c2);
        assertEquals(0, result);
        c1 = Color.BLACK;
        result = ColorUtils.colorDifference(c1, c2);
        assertEquals(-1, result);
        result = ColorUtils.colorDifference(c2, c1);
        assertEquals(-1, result);
        for (Color c : ColorUtils.initializeXtermColors().keySet())
        {
            result = ColorUtils.colorDifference(c, c);
            assertEquals(0, result);
        }

        result = ColorUtils.colorDifference(new Color(0, 0, 0),
                                            new Color(0, 0, 10));
        assertEquals(10, result);
        result = ColorUtils.colorDifference(new Color(10, 0, 0),
                                            new Color(0, 0, 10));
        assertEquals(20, result);
        result = ColorUtils.colorDifference(new Color(10, 20, 0),
                                            new Color(0, 0, 10));
        assertEquals(40, result);

    }

    /**
     * Test of colorIntensity method, of class ColorUtils.
     */
    @Test
    public void testColorIntensity()
    {
        System.out.println("colorIntensity");
        Color c = null;
        int expResult = -1;
        int result = ColorUtils.colorIntensity(c);
        assertEquals(expResult, result);

        c = new Color(0, 0, 0);
        expResult = 0;
        result = ColorUtils.colorIntensity(c);
        assertEquals(expResult, result);

        c = new Color(1, 0, 0);
        expResult = 1;
        result = ColorUtils.colorIntensity(c);
        assertEquals(expResult, result);

        c = new Color(1, 5, 0);
        expResult = 6;
        result = ColorUtils.colorIntensity(c);
        assertEquals(expResult, result);

        c = new Color(1, 5, 10);
        expResult = 16;
        result = ColorUtils.colorIntensity(c);
        assertEquals(expResult, result);
    }

    /**
     * Test of colorIntensityDifference method, of class ColorUtils.
     */
    @Test
    public void testColorIntensityDifference()
    {
        System.out.println("colorIntensityDifference");
        Color c1 = null;
        Color c2 = null;
        int expResult = 0;
        int result = ColorUtils.colorIntensityDifference(c1, c2);
        assertEquals(expResult, result);

        c1 = new Color(0, 0, 0);
        expResult = -1;
        result = ColorUtils.colorIntensityDifference(c1, c2);
        assertEquals(expResult, result);
        expResult = -1;
        result = ColorUtils.colorIntensityDifference(c2, c1);
        assertEquals(expResult, result);

        for (Color c : ColorUtils.initializeXtermColors().keySet())
        {
            result = ColorUtils.colorIntensityDifference(c, c);
            assertEquals(0, result);
        }

        result = ColorUtils.colorIntensityDifference(new Color(0, 0, 0),
                                                     new Color(0, 0, 10));
        assertEquals(10, result);
        result = ColorUtils.colorIntensityDifference(new Color(10, 0, 0),
                                                     new Color(0, 0, 10));
        assertEquals(0, result);
        result = ColorUtils.colorIntensityDifference(new Color(10, 20, 0),
                                                     new Color(0, 0, 10));
        assertEquals(20, result);
    }

    /**
     * Test of contrastColorByComplement method, of class ColorUtils.
     */
    @Test
    public void testSuitableComplementColor()
    {
        System.out.println("suitableComplementColor");
        Color color = null;
        Color expResult = Color.WHITE;
        Color result = ColorUtils.contrastColorByComplement(color);
        assertEquals(expResult, result);

        color = Color.BLACK;
        expResult = Color.WHITE;
        result = ColorUtils.contrastColorByComplement(color);
        assertEquals(expResult, result);

        color = Color.BLUE;
        expResult = new Color(255, 255, 0);
        result = ColorUtils.contrastColorByComplement(color);
        assertEquals(expResult, result);

        for (Color c1 : ColorUtils.initializeXtermColors().keySet())
        {
            Color c2 = ColorUtils.contrastColorByComplement(c1);
            int colorDiff = ColorUtils.colorDifference(c1, c2);
            int intensityDiff = ColorUtils.colorIntensityDifference(c1, c2);
            assertTrue("colorDiff (" +
                       ColorUtils.xtermColorString(c1) +
                       "," +
                       ColorUtils.xtermColorString(c2) +
                       ") should be greater than current (" +
                       150 +
                       ")=" + colorDiff,
                       colorDiff > 150);
//            assertTrue("intensityDiff (" +
//                       ColorUtils.xtermColorString(c1) +
//                       "," +
//                       ColorUtils.xtermColorString(c2) +
//                       ") should be greater than current (" +
//                       150 +
//                       ")=" + intensityDiff,
//                       intensityDiff > 150);
        }

    }

    Color ratioAvgColor(Color topLeft,
                        Color topRight,
                        Color bottomLeft,
                        Color bottomRight,
                        final double ratio_x,
                        final double ratio_y,
                        MultipleGradientPaint.CycleMethod cycleMethod)
    {
        return ColorUtils.FourColorGradientPaint.ratioAvgColor(
                topLeft,
                topRight,
                bottomLeft,
                bottomRight,
                ratio_x,
                ratio_y,
                cycleMethod);
    }

    /**
     * Test of contrastColorByComplement method, of class ColorUtils.
     */
    @Test
    public void testRatioAvgColor()
    {
        System.out.println("ratioAvgColor");
        Color topLeft = Color.RED;
        Color topRight = Color.GREEN;
        Color bottomLeft = Color.BLUE;
        Color bottomRight = Color.YELLOW;

        Color result = ratioAvgColor(topLeft,
                                     topRight,
                                     bottomLeft,
                                     bottomRight,
                                     0.0,
                                     0.0,
                                     MultipleGradientPaint.CycleMethod.NO_CYCLE);
        assertEquals(bottomRight, result);
        result = ratioAvgColor(topLeft,
                               topRight,
                               bottomLeft,
                               bottomRight,
                               1.0,
                               0.0,
                               MultipleGradientPaint.CycleMethod.NO_CYCLE);
        assertEquals(bottomLeft, result);
        result = ratioAvgColor(topLeft,
                               topRight,
                               bottomLeft,
                               bottomRight,
                               0.0,
                               1.0,
                               MultipleGradientPaint.CycleMethod.NO_CYCLE);
        assertEquals(topRight, result);
        result = ratioAvgColor(topLeft,
                               topRight,
                               bottomLeft,
                               bottomRight,
                               1.0,
                               1.0,
                               MultipleGradientPaint.CycleMethod.NO_CYCLE);
        assertEquals(topLeft, result);

        result = ratioAvgColor(topLeft,
                               topRight,
                               bottomLeft,
                               bottomRight,
                               -1.0,
                               -1.0,
                               MultipleGradientPaint.CycleMethod.NO_CYCLE);
        assertEquals(bottomRight, result);
    }

}
