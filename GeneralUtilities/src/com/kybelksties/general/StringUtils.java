
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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.openide.util.NbBundle;

/**
 * A collection of utilities for the manipulation of strings,
 *
 * @author kybelksd
 */
public class StringUtils
{

    private static final Class CLAZZ = StringUtils.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Shortcut to the platform dependent new-line character sequence.
     */
    public static final String NEWLINE = System.getProperty("line.separator");

    /**
     * The valid string representations of the boolean value true.
     */
    public static final String[] TRUE_VALUES = NbBundle.getMessage(
                                 CLAZZ,
                                 "StringUtils.trueValues").split(",");

    /**
     * The valid string representations of the boolean value true as a set (for
     * efficiency).
     */
    public static final Set<String> trueStrings = new HashSet<>(
                                    Arrays.asList(TRUE_VALUES));

    /**
     * The valid string representations of the boolean value false as a set (for
     * efficiency).
     */
    public static final String[] FALSE_VALUES = NbBundle.getMessage(
                                 CLAZZ,
                                 "StringUtils.falseValues").split(",");

    /**
     * The valid string representations of the boolean value false.
     */
    public static final Set<String> falseStrings = new HashSet<>(
                                    Arrays.asList(FALSE_VALUES));

    /**
     * Replace whitespace-sequences within the string by a single space ' ' and
     * trim the front and back.
     *
     * @param orig the original string
     * @return the trimmed and standardized string
     */
    public static String trimWhitespace(String orig)
    {
        if (orig == null)
        {
            return null;
        }
        String reval = "";
        boolean firstOccurence = true;
        for (char c : orig.toCharArray())
        {
            if (c == ' ' || c == '\t')
            {
                if (firstOccurence)
                {
                    reval += ' ';
                    firstOccurence = false;
                }
            }
            else
            {
                reval += c;
                firstOccurence = true;
            }
        }
        return reval.trim();
    }

    /**
     * Classify a String representation of a number into a number class.
     *
     * @param str the stringised value
     * @return the classification
     */
    public static NumberClass classifyNumberString(String str)
    {
        if (str == null)
        {
            return NumberClass.NONE;
        }

        String lstr = str.trim();
        if (lstr.isEmpty())
        {
            return NumberClass.NONE;
        }
        // if the string ends in ".0" then treat as INT
        if (lstr.endsWith(".0"))
        {
            lstr = lstr.substring(0, lstr.length() - 2);
        }
        boolean parsesAsFloat = true;
        boolean parsesAsInt = true;

        try
        {
            Double.parseDouble(lstr);
        }
        catch (NumberFormatException e)
        {
            parsesAsFloat = false;
        }

        try
        {
            Long.parseLong(lstr);
        }
        catch (NumberFormatException e)
        {
            parsesAsInt = false;
        }

        if (!parsesAsFloat && !parsesAsInt)
        {
            return NumberClass.NONE;
        }
        boolean isSigned = (str.charAt(0) == '-' || str.charAt(0) == '+');
        if (parsesAsInt && !isSigned)
        {
            return NumberClass.UINT;
        }
        if (parsesAsFloat && !isSigned)
        {
            return NumberClass.UFLOAT;
        }
        if (parsesAsInt)
        {
            return NumberClass.INT;
        }
        if (parsesAsFloat)
        {
            return NumberClass.FLOAT;
        }
        return NumberClass.NONE;
    }

    /**
     * Classify an array of strings returning the most restrictive NumberClass
     * that all can be classified to. If all the objects can be scanned as
     * boolean values, then the common class is boolean, if some objects can be
     * scanned as integers but some can only be scanned as Floats then the most
     * restrictive class would be Float.
     *
     * @param arr the array to find a common class for
     * @return the most restrictive NumberClass
     */
    public static NumberClass commonStringClassification(Object[] arr)
    {
        return arr == null ?
               NumberClass.NONE :
               commonStringClassification(Arrays.asList(arr));
    }

    /**
     * Classify an array of strings returning the most restrictive NumberClass
     * that all can be classified to.
     *
     * @param iterable the iterable to find a common class for
     * @return the most restrictive NumberClass
     */
    public static NumberClass commonStringClassification(Iterable iterable)
    {
        if (iterable == null)
        {
            return NumberClass.NONE;
        }
        NumberClass reval = null;
        for (Object valueObj : iterable)
        {
            String value = valueObj.toString();
            NumberClass valueClass = classifyNumberString(value);
            // first time round the
            if (reval == null)
            {
                reval = valueClass; // either NONE or a number class
            }
            else
            {
                // NONE is the smallest value, so if we get a bigger value
                // then we have a number. Set it as reval if we have not already
                // set to one of the none-number values
                if (valueClass.ordinal() > reval.ordinal())
                {
                    reval = valueClass;
                }

                if (valueClass == NumberClass.INT && reval == NumberClass.UFLOAT)
                {
                    reval = NumberClass.FLOAT;
                }

                if (valueClass == NumberClass.NONE)
                {
                    // cannot classify as number, but maybe as boolean or
                    // character.
                    Boolean bool = scanBoolString(value);
                    if (bool != null &&
                        (reval == NumberClass.BOOL || reval == NumberClass.NONE))
                    {
                        reval = NumberClass.BOOL;
                    }
                    else if (value.length() == 1 && reval != NumberClass.STRING)
                    {
                        reval = NumberClass.CHAR;
                    }
                    else
                    {
                        reval = NumberClass.STRING;
                        // here we can actually return as we can no longer have
                        // any other class for the collection
                        return reval;
                    }
                }
            }
        }

        return reval == null ? NumberClass.NONE : reval;
    }

    /**
     * Try to scan a string representation of a boolean by most common values
     * like True, on/Off/...
     *
     * @param str the string to scan
     * @return true/false if the string is scannable, null otherwise
     */
    public static Boolean scanBoolString(String str)
    {
        if (str == null)
        {
            return null;
        }
        String val = str.toLowerCase().trim();
        if (trueStrings.contains(val))
        {
            return true;
        }
        if (falseStrings.contains(val))
        {
            return false;
        }
        return null;
    }

    /**
     * Use reflection to recursively create a String containing all fields.
     *
     * @param o the Object to dump
     * @return a string representation of the fields
     */
    public static String dump(Object o)
    {
        return dump(o, 0);
    }

    private static String dump(Object object, int callCount)
    {
        callCount++;
        StringBuilder tabs = new StringBuilder();
        for (int k = 0; k < callCount; k++)
        {
            tabs.append("\t");
        }
        StringBuilder buffer = new StringBuilder();
        Class objectClass = object.getClass();
        System.out.println("objectClass=" + objectClass.toString());
        if (objectClass.isArray())
        {
            buffer.append(NEWLINE);
            buffer.append(tabs.toString());
            buffer.append("[");
            for (int i = 0; i < Array.getLength(object); i++)
            {
                if (i < 0)
                {
                    buffer.append(",");
                }
                Object value = Array.get(object, i);
                if (value != null)
                {
                    if (value.getClass().isPrimitive() ||
                        value.getClass() == Boolean.class ||
                        value.getClass() == Character.class ||
                        value.getClass() == Byte.class ||
                        value.getClass() == Short.class ||
                        value.getClass() == Integer.class ||
                        value.getClass() == Long.class ||
                        value.getClass() == Float.class ||
                        value.getClass() == Double.class ||
                        value.getClass() == String.class ||
                        value.getClass() == Void.class)
                    {
                        buffer.append(value);
                    }
                    else if (value != object)
                    {
                        buffer.append(dump(value, callCount));
                    }
                }
                else
                {
                    buffer.append("<NULL>");
                }
            }
            buffer.append(tabs.toString());
            buffer.append("]").append(NEWLINE);
        }
        else
        {
            buffer.append(NEWLINE);
            buffer.append(tabs.toString());
            buffer.append("{").append(NEWLINE);
            while (objectClass != null)
            {
                Field[] fields = objectClass.getDeclaredFields();
                for (Field field : fields)
                {
                    buffer.append(tabs.toString());
                    field.setAccessible(true);
                    buffer.append(field.getName());
                    buffer.append("=");
                    try
                    {
                        Object value = field.get(object);

                        if (value != null && value != object)
                        {

                            if (value.getClass().isPrimitive() ||
                                value.getClass() == Boolean.class ||
                                value.getClass() == Character.class ||
                                value.getClass() == Byte.class ||
                                value.getClass() == Short.class ||
                                value.getClass() == Integer.class ||
                                value.getClass() == Long.class ||
                                value.getClass() == Float.class ||
                                value.getClass() == Double.class ||
                                value.getClass() == String.class ||
                                value.getClass() == Void.class)
                            {
                                buffer.append(value);
                            }
                            else
                            {
                                buffer.append(dump(value, callCount));
                            }

                        }
                    }
                    catch (IllegalAccessException e)
                    {
                        buffer.append(e.getMessage());
                    }
                    buffer.append(NEWLINE);
                }
                objectClass = objectClass.getSuperclass();
            }
            buffer.append(tabs.toString());
            buffer.append("}").append(NEWLINE);
        }
        return buffer.toString();
    }

    /**
     * This enumeration is used to classify a String value into number-classes.
     */
    public static enum NumberClass
    {

        /**
         * Indicates that the value cannot be classified in any of the other
         * classes. This is not a number (it is a free man!)
         */
        NONE,
        /**
         * An unsigned integer.
         */
        UINT,
        /**
         * A signed integer.
         */
        INT,
        /**
         * A positive floating point number.
         */
        UFLOAT,
        /**
         * A floating point number.
         */
        FLOAT,
        /**
         * A boolean value. Not a number.
         */
        BOOL,
        /**
         * A single character. Not a number.
         */
        CHAR,
        /**
         * A character string. Not a number.
         */
        STRING
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
         * C/C++/Java comment: "/*", "*\/".
         */
        C_COMMENT;

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
                   this == C_COMMENT ? "/*" : "";
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
                   this == C_COMMENT ? "*/" : "";
        }
    }

}
