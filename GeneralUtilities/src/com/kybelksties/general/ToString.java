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

import static com.kybelksties.general.StringUtils.NEWLINE;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A set of static functions to convert collections into strings.
 *
 * @author Dieter J Kybelksties
 */
public class ToString
{

    private static final Class CLAZZ = ToString.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static HashMap<Class, ToString> class2ToString = initialize();

    /**
     * Initialize the attribute map that configures the different attributes per
     * collection class.
     *
     * @return modified static member-map
     */
    public static HashMap<Class, ToString> initialize()
    {
        class2ToString = new HashMap<>();
        class2ToString.put(Object.class, new ToString());
        class2ToString.put(Object[].class, new ToString(BracketType.BRACKET));
        class2ToString.put(Iterable.class,
                           new ToString(BracketType.SLASH, false));
        class2ToString.put(Map.class, new ToString(BracketType.CHEFRON));
        class2ToString.put(List.class, new ToString(BracketType.ROUND));
        class2ToString.put(Set.class, new ToString(BracketType.BRACE));

        return class2ToString;

    }

    static ToString getToString(Object o)
    {
        ToString reval = class2ToString.get(Object.class);
        if (o instanceof List)
        {
            reval = class2ToString.get(List.class);
        }
        else if (o instanceof Set)
        {
            reval = class2ToString.get(Set.class);
        }
        else if (o instanceof Map)
        {
            reval = class2ToString.get(Map.class);
        }
        else if (o instanceof Iterable)
        {
            reval = class2ToString.get(Iterable.class);
        }
        else if (o instanceof Object[])
        {
            reval = class2ToString.get(Object[].class);
        }
        return reval;
    }

    /**
     * Set the delimiters for a particular (collection) class/
     *
     * @param clazz The class for which attributes are changed
     * @param left  left bracket
     * @param inner inner delimiter
     * @param right right bracket
     */
    public static void setDelimiters(Class clazz,
                                     String left,
                                     String inner,
                                     String right)
    {
        if (!class2ToString.containsKey(clazz))
        {
            class2ToString.put(clazz, new ToString());
        }
        class2ToString.get(clazz).setDelimiters(left, left, left);
    }

    /**
     * Set the delimiters for a particular (collection) class
     *
     * @param clazz       The class for which attributes are changed
     * @param bracketType predefined BracketType set
     */
    public static void setDelimiters(Class clazz, BracketType bracketType)
    {
        if (!class2ToString.containsKey(clazz))
        {
            class2ToString.put(clazz, new ToString());
        }
        class2ToString.get(clazz).setDelimiters(bracketType);
    }

    /**
     * Set that values with spaces and empty values are *NOT* quoted.
     *
     * @param clazz The class for which attributes are changed
     */
    public static void setDontQuoteSpaceStrings(Class clazz)
    {
        if (!class2ToString.containsKey(clazz))
        {
            class2ToString.put(clazz, new ToString());
        }
        class2ToString.get(clazz).setDontQuoteSpaceStrings();
    }

    /**
     * Set that values with spaces and empty values are quoted with double
     * quotes (").
     *
     * @param clazz The class for which attributes are changed
     */
    public static void setDoubleQuoteSpaceStrings(Class clazz)
    {
        if (!class2ToString.containsKey(clazz))
        {
            class2ToString.put(clazz, new ToString());
        }
        class2ToString.get(clazz).setDoubleQuoteSpaceStrings();
    }

    /**
     * Set that values with spaces and empty values are quoted with single
     * quotes (').
     *
     * @param clazz The class for which attributes are changed
     */
    public static void setSingleQuoteSpaceStrings(Class clazz)
    {
        if (!class2ToString.containsKey(clazz))
        {
            class2ToString.put(clazz, new ToString());
        }
        class2ToString.get(clazz).setSingleQuoteSpaceStrings();
    }

    /**
     * Set the key - value separator for maps.
     *
     * @param clazz     The class for which attributes are changed
     * @param keyValSep the new key value separator
     */
    public static void setKeyValueSeparator(Class clazz, String keyValSep)
    {
        if (!class2ToString.containsKey(clazz))
        {
            class2ToString.put(clazz, new ToString());
        }
        class2ToString.get(clazz).setKeyValueSeparator(keyValSep);
    }

    /**
     * Set the string to be used in case of empty values.
     *
     * @param clazz      The class for which attributes are changed
     * @param nullString the new string to be used in case of empty values
     */
    static public void setNullString(Class clazz, String nullString)
    {
        if (!class2ToString.containsKey(clazz))
        {
            class2ToString.put(clazz, new ToString());
        }
        class2ToString.get(clazz).setNullString(nullString);
    }

    /**
     * Indicate that separate values should be put on separate lines.
     *
     * @param clazz The class for which attributes are changed
     */
    static public void setUseLinebreaks(Class clazz)
    {
        if (!class2ToString.containsKey(clazz))
        {
            class2ToString.put(clazz, new ToString());
        }
        class2ToString.get(clazz).setUseLinebreaks();
    }

    /**
     * Indicate that separate values should *NOT* be put on separate lines.
     *
     * @param clazz The class for which attributes are changed
     */
    static public void setDontUseLinebreaks(Class clazz)
    {
        if (!class2ToString.containsKey(clazz))
        {
            class2ToString.put(clazz, new ToString());
        }
        class2ToString.get(clazz).setDontUseLinebreaks();
    }

    /**
     * Make a string on the object.
     *
     * @param o the object to convert
     * @return the object as a string
     */
    static public String make(Object o)
    {
        ToString inst = class2ToString.get(Object.class);
        return inst.left + (o == null ? "" : o.toString()) + inst.right;
    }

    /**
     * Make a string on two objects.
     *
     * @param o1 the first object
     * @param o2 the second object
     * @return the delimited string of the objects
     */
    static public String make(Object o1, Object o2)
    {
        ToString inst = class2ToString.get(Object.class);
        return inst.left + (inst.useLinebreak ? NEWLINE : "") +
               (o1 == null ? inst.nullString : o1.toString()) +
               inst.inner + (inst.useLinebreak ? NEWLINE + "\t" : "") +
               (o2 == null ? inst.nullString : o2.toString()) +
               (inst.useLinebreak ? NEWLINE : "") + inst.right;
    }

    /**
     * Make a string of an array of chars.
     *
     * @param ar the char array
     * @return the delimited string of the objects
     */
    static public String make(char[] ar)
    {
        if (ar == null)
        {
            return "";
        }
        Character[] arChar = new Character[ar.length];
        for (int i = 0; i < ar.length; i++)
        {
            arChar[i] = ar[i];
        }

        return make(arChar);
    }

    /**
     * Make a string of an array of chars.
     *
     * @param ar the char array
     * @return the delimited string of the objects
     */
    static public String make(byte[] ar)
    {
        if (ar == null)
        {
            return "";
        }
        Byte[] arByte = new Byte[ar.length];
        for (int i = 0; i < ar.length; i++)
        {
            arByte[i] = ar[i];
        }

        return make(arByte);
    }

    /**
     * Make a string of an array of shorts.
     *
     * @param ar the short array
     * @return the delimited string of the objects
     */
    static public String make(short[] ar)
    {
        if (ar == null)
        {
            return "";
        }
        Short[] arShort = new Short[ar.length];
        for (int i = 0; i < ar.length; i++)
        {
            arShort[i] = ar[i];
        }

        return make(arShort);
    }

    /**
     * Make a string of an array of integers.
     *
     * @param ar the int array
     * @return the delimited string of the objects
     */
    static public String make(int[] ar)
    {
        if (ar == null)
        {
            return "";
        }
        Integer[] arInt = new Integer[ar.length];
        for (int i = 0; i < ar.length; i++)
        {
            arInt[i] = ar[i];
        }

        return make(arInt);
    }

    /**
     * Make a string of an array of longs.
     *
     * @param ar the int array
     * @return the delimited string of the objects
     */
    static public String make(long[] ar)
    {
        if (ar == null)
        {
            return "";
        }
        Long[] arLong = new Long[ar.length];
        for (int i = 0; i < ar.length; i++)
        {
            arLong[i] = ar[i];
        }

        return make(arLong);
    }

    /**
     * Make a string of an array of doubles.
     *
     * @param ar the double array
     * @return the delimited string of the objects
     */
    static public String make(double[] ar)
    {
        if (ar == null)
        {
            return "";
        }
        Double[] arDbl = new Double[ar.length];
        for (int i = 0; i < ar.length; i++)
        {
            arDbl[i] = ar[i];
        }

        return make(arDbl);
    }

    /**
     * Make a string of an array of floats.
     *
     * @param ar the float array
     * @return the delimited string of the objects
     */
    static public String make(float[] ar)
    {
        if (ar == null)
        {
            return "";
        }
        Float[] arFloat = new Float[ar.length];
        for (int i = 0; i < ar.length; i++)
        {
            arFloat[i] = ar[i];
        }

        return make(arFloat);
    }

    /**
     * Make a string of an array of objects.
     *
     * @param <T> generic type of array elements
     * @param ar  the object array
     * @return the delimited string of the objects
     */
    static public <T> String make(T[] ar)
    {
        if (ar == null)
        {
            return make((Object) null);
        }

        if (ar.length == 1)
        {
            return make(ar[0]);
        }

        ToString inst = getToString(ar);
        String separator = (inst.useLinebreak ?
                            inst.inner + NEWLINE + "\t" :
                            inst.inner);
        String reval = inst.left + (inst.useLinebreak ? NEWLINE + "\t" : "");

        for (int i = 0; i < ar.length; i++)
        {
            String val = ar[i] == null || ar[i].toString().isEmpty() ?
                         inst.nullString :
                         ar[i].toString();
            reval +=
            ((val.contains(" ") || val.isEmpty()) ? inst.quote() : "") +
            val +
            ((val.contains(" ") || val.isEmpty()) ? inst.quote() : "") +
            (i < ar.length - 1 ? separator : "");
        }
        reval += (inst.useLinebreak ? NEWLINE : "") + inst.right;

        return reval;
    }

    /**
     * Make a string of an iterable (collection) of objects.
     *
     * @param <T>      generic type of array elements
     * @param iterable the object collection
     * @return the delimited string of the objects
     */
    static public <T> String make(Iterable<T> iterable)
    {
        if (iterable == null)
        {
            return make((T) null);
        }

        ToString inst = getToString(iterable);
        String reval = inst.left;
        String separator = (inst.useLinebreak ?
                            inst.inner + NEWLINE + "\t" :
                            inst.inner);
        Iterator iter = iterable.iterator();
        boolean hasAtLeastTwo = false;
        if (iter.hasNext())
        {
            Iterator iter2 = iterable.iterator();
            iter2.next();
            if (iter2.hasNext())
            {
                hasAtLeastTwo = true;
            }
        }
        if (hasAtLeastTwo && inst.useLinebreak)
        {
            reval += NEWLINE + "\t";
        }
        while (iter.hasNext())
        {
            Object o = iter.next();
            String val = o != null ? o.toString() : inst.nullString;
            reval += val.contains(" ") || val.isEmpty() ? inst.quote() : "";
            reval += val.isEmpty() ? inst.nullString : val;
            reval += val.contains(" ") || val.isEmpty() ? inst.quote() : "";
            reval += iter.hasNext() ? separator : "";
        }
        reval += (hasAtLeastTwo && inst.useLinebreak ? NEWLINE : "") +
                 inst.right;

        return reval;
    }

    /**
     * Make a string of a map of objects.
     *
     * @param <T> generic type of array elements
     * @param map the map
     * @return the delimited string of the key - value pairs
     */
    static public <T> String make(Map map)
    {
        if (map == null)
        {
            return make((Object) null);
        }

        ToString inst = getToString(map);
        String reval = inst.left;
        String separator = (inst.useLinebreak ?
                            inst.inner + NEWLINE + "\t" :
                            inst.inner);
        String keyValueSeparator = inst.getKeyValueSeparator();
        Iterator keyIter = map.keySet().iterator();
        if (map.size() > 1 && inst.useLinebreak)
        {
            reval += NEWLINE + "\t";
        }
        while (keyIter.hasNext())
        {
            String key = keyIter.next().toString();
            reval += key.contains(" ") || key.isEmpty() ? inst.quote() : "";
            reval += key;
            reval += keyValueSeparator;

            String val = map.get(key).toString();
            reval += val.contains(" ") || val.isEmpty() ? inst.quote() : "";
            reval += val.isEmpty() ? inst.nullString : val;
            reval += val.contains(" ") || val.isEmpty() ? inst.quote() : "";

            reval += keyIter.hasNext() ? separator : "";
        }
        if (map.size() > 1 && inst.useLinebreak)
        {
            reval += NEWLINE;
        }
        reval += inst.right;

        return reval;
    }

    private String left = "";
    private String inner = " ";
    private String right = "";
    private boolean useLinebreak = true;
    private Character spaceQuote = '\"';
    private String nullString = "";
    private String keyValueSeparator = null;

    private ToString()
    {
        this(" ");
    }

    private ToString(BracketType bt, boolean useLinebreak)
    {
        this(bt.left(), bt.inner(), bt.right(), useLinebreak, "");
    }

    private ToString(BracketType bt)
    {
        this(bt.left(), bt.inner(), bt.right(), true, "");
    }

    /**
     * Construct with inner separator only.
     *
     * @param inner separator between elements
     */
    private ToString(String inner)
    {
        this("", inner, "");
    }

    /**
     * Construct with left separator, inner separator and right separator.
     *
     * @param left  separator on the left side, opening bracket
     * @param inner separator between elements
     * @param right separator on the right side, closing bracket
     */
    private ToString(String left, String inner, String right)
    {
        this(left, inner, right, "");
    }

    /**
     * Construct with left separator, inner separator and right separator.
     *
     * @param left       separator on the left side, opening bracket
     * @param inner      separator between elements
     * @param right      separator on the right side, closing bracket
     * @param nullString String to use if a value is null
     */
    private ToString(String left, String inner, String right, String nullString)
    {
        this(left, inner, right, true, nullString);
    }

    /**
     * Construct with left separator, inner separator and right separator.
     *
     * @param left         separator on the left side, opening bracket
     * @param inner        separator between elements
     * @param right        separator on the right side, closing bracket
     * @param useLinebreak use line-breaks if true and don't if false
     * @param nullString   String to use if a value is null
     */
    private ToString(String left,
                     String inner,
                     String right,
                     boolean useLinebreak,
                     String nullString)
    {
        this.useLinebreak = useLinebreak;
        this.left = left == null ? "" : left;
        this.inner = inner == null ? "" : inner;
        this.right = right == null ? "" : right;
        this.nullString = nullString == null ? "" : nullString;
    }

    private String getKeyValueSeparator()
    {
        return keyValueSeparator == null ? "->" : keyValueSeparator;
    }

    /**
     * Configure to use line-breaks after elements.
     */
    public void setUseLinebreaks()
    {
        useLinebreak = true;
    }

    /**
     * Configure not to use line-breaks after elements.
     */
    public void setDontUseLinebreaks()
    {
        useLinebreak = false;
    }

    private void setDelimiters(BracketType type)
    {
        if (type == null)
        {
            type = BracketType.NONE;
        }
        this.left = type.left();
        this.inner = type.inner();
        this.right = type.right();
    }

    private void setDelimiters(String left, String inner, String right)
    {
        if (left == null)
        {
            left = "";
        }
        this.left = left;
        if (inner == null)
        {
            inner = " ";
        }
        this.inner = inner;
        if (right == null)
        {
            right = "";
        }
        this.right = right;
    }

    private void setNullString(String nullString)
    {
        this.nullString = nullString == null ? "" : nullString;
    }

    private void setSingleQuoteSpaceStrings()
    {
        spaceQuote = '\'';
    }

    private void setDoubleQuoteSpaceStrings()
    {
        spaceQuote = '\"';
    }

    private void setDontQuoteSpaceStrings()
    {
        spaceQuote = null;
    }

    private void setKeyValueSeparator(String keyValueSeparator)
    {
        this.keyValueSeparator = keyValueSeparator;
    }

    private String quote()
    {
        return spaceQuote == null ? "" : spaceQuote.toString();
    }

    /**
     * Enumeration of common bracket types.
     */
    public enum BracketType
    {

        /**
         * No brackets.
         */
        NONE,
        /**
         * Curly brackets: "{", "}".
         */
        BRACE,
        /**
         * Square brackets: "[", "]".
         */
        BRACKET,
        /**
         * Chevron brackets: "<", ">".
         */
        CHEFRON,
        /**
         * Round brackets: "(", ")".
         */
        ROUND,
        /**
         * Pipe brackets: "|", "|".
         */
        PIPE,
        /**
         * Slash brackets: "/","/".
         */
        SLASH,
        /**
         * Backslash brackets: "\","\".
         */
        BACKSLASH;

        /**
         * Get the left (opening) bracket.
         *
         * @return the left bracket as String.
         */
        public String left()
        {
            return this == NONE ? "" :
                   this == BRACE ? "{" :
                   this == BRACKET ? "[" :
                   this == CHEFRON ? "<" :
                   this == ROUND ? "(" :
                   this == PIPE ? "|" :
                   this == SLASH ? "/" :
                   this == BACKSLASH ? "\\" :
                   "";
        }

        /**
         * Get the inner separator.
         *
         * @return the left bracket as String.
         */
        public String inner()
        {
            return this == NONE ? " " :
                   this == BRACE ? "," :
                   this == BRACKET ? "," :
                   this == CHEFRON ? "," :
                   this == ROUND ? "," :
                   this == PIPE ? "|" :
                   this == SLASH ? "/" :
                   this == BACKSLASH ? "\\" :
                   "";
        }

        /**
         * Get the right (closing) bracket.
         *
         * @return the right bracket as String.
         */
        public String right()
        {
            return this == NONE ? "" :
                   this == BRACE ? "}" :
                   this == BRACKET ? "]" :
                   this == CHEFRON ? ">" :
                   this == ROUND ? ")" :
                   this == PIPE ? "|" :
                   this == SLASH ? "/" :
                   this == BACKSLASH ? "\\" :
                   "";
        }
    }

}
