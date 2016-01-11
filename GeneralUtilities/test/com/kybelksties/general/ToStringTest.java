
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the public interface of the ToString class.
 *
 * @author kybelksd
 */
public class ToStringTest
{

    private static final Logger LOG =
                                Logger.getLogger(ToStringTest.class.getName());

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
    public ToStringTest()
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
     * Test of make method, of class ToString.
     */
    @Test
    public void testMake()
    {
        System.out.println("make Strings from various containers and objects");
        ToString.initialize();
        String expResult = "[\n\tx,\n\ty,\n\tz\n]";
        String result = ToString.make(e.values());
        assertEquals(expResult, result);
        String[] ar = new String[]
         {
             "string1", "string 2", "S S S S S", "", "last"
        };
        expResult =
        "[\n\tstring1,\n\t\"string 2\",\n\t\"S S S S S\",\n\t\"\",\n\tlast\n]";
        result = ToString.make(ar);
        assertEquals(expResult, result);

        ArrayList<String> arrList = new ArrayList<>();
        arrList.addAll(Arrays.asList(ar));
        result = ToString.make(arrList);
        expResult =
        "(\n\tstring1,\n\t\"string 2\",\n\t\"S S S S S\",\n\t\"\",\n\tlast\n)";
        assertEquals(expResult, result);

        TreeMap<String, String> map = new TreeMap<>();
        expResult = "<>";
        result = ToString.make(map);
        assertEquals(expResult, result);
        expResult = "<01->one>";
        map.put("01", "one");
        result = ToString.make(map);
        assertEquals(expResult, result);
        map.put("02", "two");
        map.put("03", "three");
        expResult = "<\n\t01->one,\n" +
                    "\t02->two,\n" +
                    "\t03->three\n>";
        result = ToString.make(map);
        assertEquals(expResult, result);
        result = ToString.make(map);
        assertEquals(expResult, result);

        TreeSet<String> set = new TreeSet<>();
        expResult = "{}";
        result = ToString.make(set);
        assertEquals(expResult, result);
        set.add("el1");
        expResult = "{el1}";
        result = ToString.make(set);
        assertEquals(expResult, result);

        set.add("el2");
        expResult = "{\n\tel1,\n\tel2\n}";
        result = ToString.make(set);
        assertEquals(expResult, result);
    }

    /**
     * Test of make method, of class ToString.
     */
    @Test
    public void testChangeProperties()
    {
        System.out.println("change properties");
        ToString.initialize();

        // change the delimiters for arrays.
        ToString.setDelimiters(Object[].class, ToString.BracketType.CHEFRON);

        String expResult = "<\n\tx,\n\ty,\n\tz\n>";
        String result = ToString.make(e.values());
        assertEquals(expResult, result);

        String[] ar = new String[]
         {
             "string1", "string 2", "S S S S S", "", "last"
        };
        expResult =
        "<\n\tstring1,\n\t\"string 2\",\n\t\"S S S S S\",\n\t\"\",\n\tlast\n>";
        result = ToString.make(ar);
        assertEquals(expResult, result);

        // Change to no linebreaks for lists.
        ToString.setDontUseLinebreaks(List.class);
        ArrayList<String> arrList = new ArrayList<>();
        arrList.addAll(Arrays.asList(ar));
        result = ToString.make(arrList);
        expResult =
        "(string1,\"string 2\",\"S S S S S\",\"\",last)";
        assertEquals(expResult, result);

        // Change the ke/value-separator fot maps.
        ToString.setKeyValueSeparator(Map.class, ">>>");
        TreeMap<String, String> map = new TreeMap<>();
        expResult = "<>";
        result = ToString.make(map);
        assertEquals(expResult, result);
        expResult = "<01>>>one>";
        map.put("01", "one");
        result = ToString.make(map);
        assertEquals(expResult, result);
        map.put("02", "two");
        map.put("03", "three");
        expResult = "<\n\t01>>>one,\n" +
                    "\t02>>>two,\n" +
                    "\t03>>>three\n>";
        result = ToString.make(map);
        assertEquals(expResult, result);
        result = ToString.make(map);
        assertEquals(expResult, result);

        // Change quotes for empty and whitespace values.
        ToString.setSingleQuoteSpaceStrings(Set.class);
        TreeSet<String> set = new TreeSet<>();
        expResult = "{}";
        result = ToString.make(set);
        assertEquals(expResult, result);
        set.add("el 1");
        expResult = "{'el 1'}";
        result = ToString.make(set);
        assertEquals(expResult, result);

        set.add("el 2");
        expResult = "{\n\t'el 1',\n\t'el 2'\n}";
        result = ToString.make(set);
        System.out.println("result =" + result);
        System.out.println("expResult =" + expResult);
        assertEquals(expResult, result);

        // Don't use quotes for empty and whitespace values
        // and set a different null-value.
        ToString.setDontQuoteSpaceStrings(Set.class);
        ToString.setNullString(Set.class, "<NULL>");
        set.add("");
        expResult = "{\n\t<NULL>,\n\tel 1,\n\tel 2\n}";
        result = ToString.make(set);
        System.out.println("result =" + result);
        System.out.println("expResult =" + expResult);
        assertEquals(expResult, result);
    }

    enum e
    {

        x, y, z
    }

}
