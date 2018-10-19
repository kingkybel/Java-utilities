/*
 * Copyright (C) 2018 Dieter J Kybelksties
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
 * @date: 2018-09-13
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.general;

import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the extension of the BitSet class.
 *
 * @author kybelksd
 */
public class ExtBitSetTest
{

    private static final Class CLAZZ = ExtBitSetTest.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Setup before this is created.
     */
    @BeforeClass
    public static void setUpClass()
    {
    }

    /**
     * Run this after this has been deleted.
     */
    @AfterClass
    public static void tearDownClass()
    {
    }

    /**
     * Default construct.
     */
    public ExtBitSetTest()
    {
    }

    /**
     * Set up before test.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Clean-up after test.
     */
    @After
    public void tearDown()
    {
    }

    /**
     * Test of bitIsSet method, of class ExtBitSet.
     */
    @Test
    public void testBitIsSet()
    {
        System.out.println("bitIsSet");
        boolean result;

        System.out.println("-------- null ----------");
        ExtBitSet bitSet = new ExtBitSet(null);
        result = bitSet.get(0);
        assertEquals(false, result);
        System.out.println(
                bitSet.toString() + " [" + 0 + "] =" + bitSet.get(0) +
                " expected=" + false);

        System.out.println("-------- int ----------");
        bitSet = new ExtBitSet(1);// 00001
        result = bitSet.get(0);
        assertEquals(true, result);
        System.out.println(
                bitSet.toString() + " [" + 0 + "] =" + bitSet.get(0) +
                " expected=" + true);

        bitSet = new ExtBitSet(0);// 00000
        result = bitSet.get(0);
        assertEquals(false, result);
        System.out.println(
                bitSet.toString() + " [" + 0 + "] =" + bitSet.get(0) +
                " expected=" + false);

        bitSet = new ExtBitSet(2);// 00010
        result = bitSet.get(0);
        assertEquals(false, result);
        System.out.println(
                bitSet.toString() + " [" + 0 + "] =" + bitSet.get(0) +
                " expected=" + false);
        result = bitSet.get(1);
        assertEquals(true, result);
        System.out.println(
                bitSet.toString() + " [" + 0 + "] =" + bitSet.get(0) +
                " expected=" + true);

        bitSet = new ExtBitSet(Integer.MAX_VALUE);// 111...111
        for (int i = 0; i < 31; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        bitSet.set(31);
        for (int i = 0; i < 32; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        for (int i = 32; i < 42; i++)
        {
            result = bitSet.get(i);
            assertEquals(false, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + false);
        }
        System.out.println("-------- INT[] ----------");

        bitSet.reset(new Integer[]
        {
            Integer.MAX_VALUE, Integer.MAX_VALUE
        });
        for (int i = 0; i < 62; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        bitSet.reset(new Integer[]
        {
            Integer.MAX_VALUE, 0
        });
        for (int i = 0; i < 31; i++)
        {
            result = bitSet.get(i);
            assertEquals(false, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + false);
        }
        for (int i = 32; i < 62; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        bitSet.reset(new int[]
        {
            Integer.MAX_VALUE, 0
        });
        for (int i = 0; i < 31; i++)
        {
            result = bitSet.get(i);
            assertEquals(false, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + false);
        }
        for (int i = 32; i < 62; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }

        bitSet.reset(new int[]
        {
            Integer.MAX_VALUE, 0, Integer.MAX_VALUE
        });
        for (int i = 0; i < 31; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        for (int i = 32; i < 62; i++)
        {
            result = bitSet.get(i);
            assertEquals(false, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + false);
        }
        for (int i = 62; i < 93; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        System.out.println("-------- BOOL ----------");
        bitSet.reset(new boolean[]
        {
            false, true, false, true // 0101
        });
        System.out.println(bitSet.toString());
        result = bitSet.get(0);
        assertEquals(true, result);
        System.out.println(
                bitSet.toString() + " [" + 0 + "] =" + bitSet.get(0) +
                " expected=" + true);
        result = bitSet.get(1);
        assertEquals(false, result);
        System.out.println(
                bitSet.toString() + " [" + 1 + "] =" + bitSet.get(1) +
                " expected=" + false);
        result = bitSet.get(2);
        assertEquals(true, result);
        System.out.println(
                bitSet.toString() + " [" + 2 + "] =" + bitSet.get(2) +
                " expected=" + true);
        result = bitSet.get(3);
        assertEquals(false, result);
        System.out.println(
                bitSet.toString() + " [" + 3 + "] =" + bitSet.get(3) +
                " expected=" + false);

        System.out.println("-------- BYTE ----------");
        byte[] b = new byte[]
       {
           Byte.MAX_VALUE
        };
        bitSet.reset(b);
        for (int i = 0; i < 7; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        bitSet.set(new Byte[]
        {
            Byte.MAX_VALUE, 0, 0
        });
        System.out.println(bitSet.toString());
        for (int i = 0; i < 21; i++)
        {
            result = bitSet.get(i);
            assertEquals(i < 7 || i > 13, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + (i < 7 || i > 13));
        }
        System.out.println("-------- CHAR ----------");
        char[] c = new char[]
       {
           Character.MAX_VALUE
        };
        bitSet.reset(c);
        for (int i = 0; i < 15; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        bitSet.set(new Character[]
        {
            Character.MAX_VALUE, 0, 0
        });
        System.out.println(bitSet.toString());
        for (int i = 0; i < 45; i++)
        {
            result = bitSet.get(i);
            assertEquals(i < 15 || i > 29, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + (i < 15 || i > 29));
        }
        System.out.println("-------- short ----------");
        short[] s = new short[]
        {
            Short.MAX_VALUE
        };
        bitSet.reset(s);
        System.out.println(bitSet);
        for (int i = 0; i < 15; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        bitSet.set(new Short[]
        {
            Short.MAX_VALUE, 0, 0
        });
        for (int i = 0; i < 45; i++)
        {
            result = bitSet.get(i);
            assertEquals(i < 15 || i > 29, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + (i < 15 || i > 29));
        }

        System.out.println("-------- long ----------");
        long[] l = new long[]
       {
           Long.MAX_VALUE
        };
        bitSet.reset(l);
        System.out.println(bitSet);
        for (int i = 0; i < 63; i++)
        {
            result = bitSet.get(i);
            assertEquals(true, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + true);
        }
        bitSet.set(new Long[]
        {
            Long.MAX_VALUE, 0L, 0L
        });
        for (int i = 0; i < 189; i++)
        {
            result = bitSet.get(i);
            assertEquals(i < 63 || i > 125, result);
            System.out.println(
                    bitSet.toString() + " [" + i + "] =" + bitSet.get(i) +
                    " expected=" + (i < 63 || i > 125));
        }
    }

}
