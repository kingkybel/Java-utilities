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

import static java.lang.Math.min;
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
     * The valid default string representations of the boolean value true as a
     * set (for efficiency).
     */
    private static Set<String> trueStrings = setBooleanSynonyms(true);

    /**
     * The valid default string representations of the boolean value false.
     */
    private static Set<String> falseStrings = setBooleanSynonyms(false);

    /**
     * Resets the valid string representations of booleans to the default.
     */
    static public void resetBooleanSynonyms()
    {
        trueStrings = setBooleanSynonyms(true);
        falseStrings = setBooleanSynonyms(false);
    }

    /**
     * Resets the valid string representations of booleans replacing the system
     * default.
     *
     * @param trueSynonyms  a comma-separated list of strings to be interpreted
     *                      as "true"
     * @param falseSynonyms a comma-separated list of strings to be interpreted
     *                      as "false"
     */
    static public void resetBooleanSynonyms(String trueSynonyms,
                                            String falseSynonyms)
    {
        String[] SYNONYM_ARRAY = trueSynonyms.split(",");
        trueStrings = setBooleanSynonyms(true, SYNONYM_ARRAY);
        SYNONYM_ARRAY = falseSynonyms.split(",");
        falseStrings = setBooleanSynonyms(false, SYNONYM_ARRAY);
    }

    /**
     * Add additional representations for "true" and "false".
     *
     * @param trueSynonyms  a comma-separated list of strings to be interpreted
     *                      as "true"
     * @param falseSynonyms a comma-separated list of strings to be interpreted
     *                      as "false"
     */
    static public void addBooleanSynonyms(String trueSynonyms,
                                          String falseSynonyms)
    {
        String[] SYNONYM_ARRAY = trueSynonyms.split(",");
        trueStrings.addAll(Arrays.asList(SYNONYM_ARRAY));
        SYNONYM_ARRAY = falseSynonyms.split(",");
        falseStrings.addAll(Arrays.asList(SYNONYM_ARRAY));
    }

    static private Set<String> setBooleanSynonyms(boolean b)
    {
        String[] SYNONYM_ARRAY = NbBundle.getMessage(
                 CLAZZ,
                 b ? "StringUtils.trueValues" : "StringUtils.falseValues").
                 split(",");

        return setBooleanSynonyms(b, SYNONYM_ARRAY);
    }

    static private Set<String> setBooleanSynonyms(boolean b,
                                                  String[] SYNONYM_ARRAY)
    {
        return new HashSet<>(Arrays.asList(SYNONYM_ARRAY));
    }

    /**
     * Retrieve a set of Strings that contains valid representations of the
     * boolean "true" value.
     *
     * @return
     */
    public static Set<String> getValidTrueStrings()
    {
        return trueStrings;
    }

    /**
     * Retrieve a set of Strings that contains valid representations of the
     * boolean "false" value.
     *
     * @return
     */
    public static Set<String> getValidFalseStrings()
    {
        return falseStrings;
    }

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
     * @param str the stringized value
     * @return the classification
     */
    public static NumberSpecies findSpecies(String str)
    {
        if (str == null)
        {
            return NumberSpecies.NONE;
        }

        String lstr = str.trim();
        if (lstr.isEmpty())
        {
            return NumberSpecies.NONE;
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
            return NumberSpecies.NONE;
        }
        // it only makes sense to have a sign if one wants to handle positive
        // and negative numbers
        boolean hasSign = (str.charAt(0) == '-' || str.charAt(0) == '+');
        if (parsesAsInt && !hasSign)
        {
            return NumberSpecies.UINT;
        }
        if (parsesAsFloat && !hasSign)
        {
            return NumberSpecies.UFLOAT;
        }
        if (parsesAsInt)
        {
            return NumberSpecies.INT;
        }
        if (parsesAsFloat)
        {
            return NumberSpecies.FLOAT;
        }

        return NumberSpecies.NONE;
    }

    /**
     * Classify an array of strings returning the most restrictive NumberSpecies
     * that all can be classified to. If all the objects can be scanned as
     * boolean values, then the common class is boolean, if some objects can be
     * scanned as integers but some can only be scanned as Floats then the most
     * restrictive class would be Float.
     *
     * @param arr the array to find a common class for
     * @return the most restrictive NumberSpecies
     */
    public static NumberSpecies commonSpecies(Object[] arr)
    {
        return arr == null ?
               NumberSpecies.NONE :
               commonSpecies(Arrays.asList(arr));
    }

    /**
     * Classify an array of strings returning the most restrictive NumberSpecies
     * that all can be classified to.
     *
     * @param iterable the iterable to find a common class for
     * @return the most restrictive NumberSpecies
     */
    public static NumberSpecies commonSpecies(Iterable iterable)
    {
        if (iterable == null)
        {
            return NumberSpecies.NONE;
        }
        NumberSpecies reval = null;
        for (Object valueObj : iterable)
        {
            String value = valueObj.toString();
            NumberSpecies valueClass = findSpecies(value);
            if (reval == null)
            {
                reval = valueClass; // either NONE or a number class
            }
            // NONE is the smallest value, so if we get a bigger value
            // then we have a number. Set it as reval if we have not already
            // set to one of the none-number values
            if (valueClass.ordinal() > reval.ordinal())
            {
                reval = valueClass;
            }

            if (valueClass == NumberSpecies.INT &&
                reval == NumberSpecies.UFLOAT)
            {
                reval = NumberSpecies.FLOAT;
            }

            if (valueClass == NumberSpecies.NONE)
            {
                // cannot classify as number, but maybe as boolean or
                // character.
                Boolean bool = scanBoolString(value);
                if (bool != null &&
                    (reval == NumberSpecies.BOOL ||
                     reval == NumberSpecies.NONE))
                {
                    reval = NumberSpecies.BOOL;
                }
                else if (value.length() == 1 &&
                         reval != NumberSpecies.STRING)
                {
                    reval = NumberSpecies.CHAR;
                }
                else
                {
                    reval = NumberSpecies.STRING;
                    // here we can actually return as we can no longer have
                    // any other class for the collection
                    return reval;
                }
            }
        }

        return reval == null ? NumberSpecies.NONE : reval;
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
     * Compute Levenshtein distance.
     *
     * @param string1 first string
     * @param string2 second string
     * @return the Levenshtein distance between string1 and string2
     */
    static public int levenshteinDistance(String string1, String string2)
    {
        if (string1 == null || string1.isEmpty())
        {
            return string2 == null ? 0 : string2.length();
        }
        if (string2 == null || string2.isEmpty())
        {
            return string1.length();
        }

        int len1 = string1.length();
        int len2 = string2.length();
        int dist[][] = new int[len1 + 1][len2 + 1];

        for (int i1 = 0; i1 <= len1; i1++)
        {
            dist[i1][0] = i1;
        }

        for (int i2 = 0; i2 <= len2; i2++)
        {
            dist[0][i2] = i2;
        }

        for (int i1 = 1; i1 <= len1; i1++)
        {
            char c_i1 = string1.charAt(i1 - 1);

            for (int i2 = 1; i2 <= len2; i2++)
            {
                char c_i2 = string2.charAt(i2 - 1);

                dist[i1][i2] = min(min(dist[i1 - 1][i2] + 1,
                                       dist[i1][i2 - 1] + 1),
                                   dist[i1 - 1][i2 - 1] + (c_i1 == c_i2 ? 0 : 1));
            }
        }

        return dist[len1][len2];
    }

    /**
     * This enumeration is used to classify a String value into number-classes.
     */
    public static enum NumberSpecies
    {

        /**
         * Indicates that the value cannot be classified in any of the other
         * classes. This is not a number (it is a free man!)
         */
        NONE, /**
         * An unsigned integer.
         */
        UINT, /**
         * A signed integer.
         */
        INT, /**
         * A positive floating point number.
         */
        UFLOAT, /**
         * A floating point number.
         */
        FLOAT, /**
         * A boolean value. Not a number.
         */
        BOOL, /**
         * A single character. Not a number.
         */
        CHAR, /**
         * A character string. Not a number.
         */
        STRING
    }
}
