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

import java.util.Arrays;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.openide.util.NbBundle;

/**
 * A variant type only capable of holding Plain-old-data types.
 *
 * @author kybelksd
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PodVariant
{

    private static final Class CLAZZ = PodVariant.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    public static final String[] BOOLEAN_SYNONYMS = NbBundle.getMessage(
                                 CLAZZ,
                                 "PodVariant.booleanSynonyms").split(",");
    public static final String[] STRING_SYNONYMS = NbBundle.getMessage(
                                 CLAZZ,
                                 "PodVariant.stringSynonyms").split(",");
    public static final String[] FLOAT_SYNONYMS = NbBundle.getMessage(
                                 CLAZZ,
                                 "PodVariant.floatSynonyms").split(",");
    public static final String[] PATH_SYNONYMS = NbBundle.getMessage(
                                 CLAZZ,
                                 "PodVariant.pathSynonyms").split(",");
    public static final String[] INT_SYNONYMS = NbBundle.getMessage(
                                 CLAZZ,
                                 "PodVariant.intSynonyms").split(",");

    /**
     * Create a PODVariant from a string describing a type.
     *
     * @param typeStr the string describing the POD type
     * @return the new value
     */
    public static PodVariant fromString(String typeStr)
    {
        return fromString(typeStr, null);
    }

    /**
     * Create a PODVariant from a string describing a type and a value as
     * string.
     *
     * @param typeStr the string describing the POD type
     * @param value   the value as string
     * @return the new value
     */
    public static PodVariant fromString(String typeStr, String value)
    {
        Type type = Type.fromString(typeStr);
        PodVariant reval = new PodVariant(type);

        if (value != null)
        {
            switch (type)
            {
                case BOOLEAN:
                    reval.value = StringUtils.scanBoolString(value);
                    break;
                case INTEGER:
                    reval.value = Integer.parseInt(value);
                    break;
                case DOUBLE:
                    reval.value = Double.parseDouble(value);
                    break;
                case STRING:
                    reval.value = value;
                    break;
                case PATH:
                    reval.value = value;
                    break;
            }
        }

        return reval;
    }
    private Object value;
    private Type type = Type.UNDEFINED;

    /**
     * Default construct.
     */
    public PodVariant()
    {
        type = Type.UNDEFINED;
        value = null;
    }

    /**
     * Construct with a type.
     *
     * @param type the type of the new value
     */
    public PodVariant(PodVariant.Type type)
    {
        this.type = type;
        value = this.type.equals(Type.BOOLEAN) ? false :
                this.type.equals(Type.INTEGER) ? new Integer(0) :
                this.type.equals(Type.DOUBLE) ? new Double(0.0) :
                this.type.equals(Type.STRING) ? "" :
                null;
    }

    /**
     * Construct from an object, bootstrapping the type.
     *
     * @param value the value which will be
     */
    public PodVariant(Object value)
    {
        String valueClass = value == null ?
                            String.class.getName() :
                            value.getClass().getName();
        this.type = valueClass.equals(String.class.getName()) ? Type.STRING :
                    valueClass.equals(Boolean.class.getName()) ? Type.BOOLEAN :
                    valueClass.equals(Integer.class.getName()) ? Type.INTEGER :
                    valueClass.equals(Double.class.getName()) ? Type.DOUBLE :
                    Type.STRING;
        this.value = value;
    }

    /**
     * Construct a boolean variant.
     *
     * @param booleanValue
     */
    public PodVariant(Boolean booleanValue)
    {
        this.value = booleanValue;
        this.type = Type.BOOLEAN;
    }

    /**
     * Construct a string variant.
     *
     * @param stringValue
     */
    public PodVariant(String stringValue)
    {
        this.value = stringValue;
        this.type = Type.STRING;
    }

    /**
     * Construct a integer variant.
     *
     * @param integerValue
     */
    public PodVariant(Integer integerValue)
    {
        this.value = integerValue;
        this.type = Type.INTEGER;
    }

    /**
     * Construct a floating point variant.
     *
     * @param doubleValue
     */
    public PodVariant(Double doubleValue)
    {
        this.value = doubleValue;
        this.type = Type.DOUBLE;
    }

    /**
     * Retrieves whether the variant is empty.
     *
     * @return if so, false otherwise
     */
    public boolean isNull()
    {
        return type == Type.UNDEFINED;
    }

    @Override
    public String toString()
    {
        if (value == null)
        {
            System.out.println("Value=null type=" + type);
        }
        return type == Type.BOOLEAN ? ((Boolean) value ? "1" : "0") :
               type == Type.STRING ? value.toString() :
               type == Type.INTEGER ? ((Integer) value).toString() :
               type == Type.DOUBLE ? ((Double) value).toString() :
               "";
    }

    /**
     * Retrieve the type of the variant.
     *
     * @return the type
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Retrieve the value of the variant.
     *
     * @return the value
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * The plain old data variant is a boolean.
     *
     * @return true if it is and false otherwise
     */
    public boolean isBoolean()
    {
        return type == Type.BOOLEAN;
    }

    /**
     * The plain old data variant is a string.
     *
     * @return true if it is and false otherwise
     */
    public boolean isString()
    {
        return type == Type.STRING;
    }

    /**
     * The plain old data variant is a integer.
     *
     * @return true if it is and false otherwise
     */
    public boolean isInteger()
    {
        return type == Type.INTEGER;
    }

    /**
     * The plain old data variant is a floating point value.
     *
     * @return true if it is and false otherwise
     */
    public boolean isDouble()
    {
        return type == Type.DOUBLE;
    }

    /**
     * The plain old data variant is a floating point value.
     *
     * @return true if it is and false otherwise
     */
    public boolean isPath()
    {
        return type == Type.PATH;
    }

    /**
     * Retrieve the boolean value.
     *
     * @return the boolean value
     */
    public Boolean getBooleanValue()
    {
        return type == Type.BOOLEAN ? (Boolean) value : null;
    }

    /**
     * Retrieve the boolean value.
     *
     * @param booleanValue
     */
    public void setBooleanValue(Boolean booleanValue)
    {
        this.value = booleanValue;
    }

    /**
     * Retrieve the string value.
     *
     * @return the string value
     */
    public String getStringValue()
    {
        return type == Type.STRING ? (String) value : null;
    }

    /**
     * Set the boolean value.
     *
     * @param stringValue
     */
    public void setStringValue(String stringValue)
    {
        this.value = stringValue;
    }

    /**
     * Retrieve the integer value.
     *
     * @return the integer value
     */
    public Integer getIntegerValue()
    {
        return type == Type.INTEGER ? (Integer) value : null;
    }

    /**
     * Set the integer value.
     *
     * @param integerValue new integer
     */
    public void setIntegerValue(Integer integerValue)
    {
        this.value = integerValue;
    }

    /**
     * Retrieve the floating point value.
     *
     * @return the floating point value
     */
    public Double getDoubleValue()
    {
        return type == Type.DOUBLE ? (Double) value : null;
    }

    /**
     * Set the floating point value.
     *
     * @param doubleValue new double
     */
    public void setDoubleValue(Double doubleValue)
    {
        this.value = doubleValue;
    }

    /**
     * The POD types.
     */
    public enum Type
    {

        /**
         * Undefined - for empty POD values.
         */
        UNDEFINED,
        /**
         * Boolean values.
         */
        BOOLEAN,
        /**
         * String values.
         */
        STRING, /**
         * Signed integral values.
         */
        INTEGER,
        /**
         * Double precision floating point values.
         */
        DOUBLE,
        /**
         * Special string variable that can be interpreted a Unix ${PATH}
         * variables.
         */
        PATH;

        static Type fromString(String typeStr)
        {
            Type type = Type.UNDEFINED;
            String lowerTypeStr = typeStr.toLowerCase().trim();
            if (Arrays.asList(BOOLEAN_SYNONYMS).contains(lowerTypeStr))
            {
                type = Type.BOOLEAN;
            }
            else if (Arrays.asList(INT_SYNONYMS).contains(lowerTypeStr))
            {
                type = Type.INTEGER;
            }
            else if (Arrays.asList(FLOAT_SYNONYMS).contains(lowerTypeStr))
            {
                type = Type.DOUBLE;
            }
            else if (Arrays.asList(PATH_SYNONYMS).contains(lowerTypeStr))
            {
                type = Type.PATH;
            }
            else if (Arrays.asList(STRING_SYNONYMS).contains(lowerTypeStr))
            {
                type = Type.STRING;
            }
            return type;
        }
    }
}
