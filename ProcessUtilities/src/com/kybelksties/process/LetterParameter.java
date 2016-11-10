/*
 * Copyright (C) 2015 Dieter J Kybelksties
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @date: 2015-12-14
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.process;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Extends the AbstractParameter class to create the UNIX - style single letter
 * command-line parameters.
 *
 * @author kybelksd
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LetterParameter
        extends AbstractParameter
        implements Serializable
{

    private static final Class CLAZZ = LetterParameter.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private static final long serialVersionUID = -8940196742313991701L;

    private Character letter = null;
    private boolean hasArg = false;

    /**
     * Default constructor.
     */
    public LetterParameter()
    {
        this(null);
    }

    /**
     * Construct a parameter that is a letter.
     *
     * @param letter the letter used as parameter descriptor
     */
    public LetterParameter(Character letter)
    {
        this(letter, null);
        this.letter = letter;
    }

    /**
     * Construct a parameter that is a letter with an argument.
     *
     * @param letter the letter used as parameter descriptor
     * @param value  the value serves as the argument as the parameter
     */
    public LetterParameter(Character letter, String value)
    {
        this(letter, value, null);
        this.letter = letter;
        this.hasArg = true;
    }

    /**
     * Construct a parameter that is a letter with an argument, which is
     * defaulted.
     *
     * @param letter       the letter used as parameter descriptor
     * @param value        the value serves as the argument as the parameter
     * @param defaultValue a default value to be in case the argument value in
     *                     null
     */
    public LetterParameter(Character letter, String value, String defaultValue)
    {
        super(value, defaultValue);
        this.letter = letter;
        this.hasArg = true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        LetterParameter reval = new LetterParameter();
        reval.copyMembers(this);
        reval.hasArg = hasArg;
        reval.letter = letter;

        return reval;
    }

    @Override
    public String toString()
    {
        if (hasArgument() && !isMandatory() && getValue().isEmpty())
        {
            return "";
        }
        return "-" + letter.toString() + (hasArgument() ? " " + getValue() : "");
    }

    /**
     * Check whether the parameter needs an argument.
     *
     * @return true if argument is needed, false otherwise.
     */
    @Override
    public boolean hasArgument()
    {
        return hasArg;
    }

    /**
     * Add an argument that can *NOT* be customised.
     *
     * @param value the argument value
     */
    public void addFixedArgument(String value)
    {
        hasArg = true;
        setFixedValue(value);
    }

    /**
     * Add an argument that can be customised.
     *
     * @param value the argument value
     */
    public void addCustomArgument(String value)
    {
        hasArg = true;
        setCustomValue(value);
    }

    /**
     * Remove the argument from the parameter so that the parameter can longer
     * accept an argument.
     */
    public void removeArgument()
    {
        hasArg = false;
        unfixValue();
        undefaultValue();
    }

    /**
     * Compare this with the given character.
     *
     * @param letter the letter as char
     * @return true is letter equals this letter, false otherwise
     */
    public boolean letterEquals(char letter)
    {
        return this.letter.equals(letter);
    }

    /**
     * Retrieve the letter as Character.
     *
     * @return the letter
     */
    public Character getLetter()
    {
        return letter;
    }

    /**
     * Set the letter as Character.
     *
     * @param letter the new letter
     */
    public void setLetter(Character letter)
    {
        this.letter = letter;
    }

    /**
     * Retrieve the parameter as string as used on the command line.
     *
     * @return the parameter string
     */
    public String getParameterString()
    {
        return getParameterString(Style.UNIX);
    }

    /**
     * Retrieve the parameter as string as used on the command line.
     *
     * @param style style of parameter tag
     * @return the parameter string
     */
    public String getParameterString(Style style)
    {
        return (style == Style.WINDOWS ? "/" : "-") + letter;
    }

    /**
     * Enumeration of Style of letter-parameters.
     */
    public static enum Style
    {

        /**
         * Dash '-'.
         */
        UNIX,
        /**
         * Forward-slash '/'.
         */
        WINDOWS
    }

}
