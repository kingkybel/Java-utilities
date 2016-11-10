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

import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Base to formalise a few methods and properties of parameters.
 *
 * @author kybelksd
 */
@XmlTransient
@XmlSeeAlso(
        {
            LetterParameter.class, PositionalParameter.class
        })
public abstract class AbstractParameter
{

    private static final Class CLAZZ = AbstractParameter.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private boolean mandatory = false;
    private String value = null;
    private String defaultValue = null;
    private boolean fixed = false;
    private boolean optionallySelected = false;
    private boolean multipleInstances = false;

    /**
     * Default construct.
     */
    public AbstractParameter()
    {
        undefaultValue();
        unfixValue();

    }

    /**
     * Construct the parameter with a value and a default value.
     *
     * @param value        if value is not null the the parameter is constant
     * @param defaultValue can be null, if value is null then this value will be
     *                     as default
     */
    public AbstractParameter(String value, String defaultValue)
    {
        if (value != null && !value.isEmpty())
        {
            setFixedValue(value);
        }
        else if (defaultValue != null && !defaultValue.isEmpty())
        {
            unfixValue();
            setDefault(defaultValue);
        }
        else
        {
            unfixValue();
            undefaultValue();
        }
    }

    @Override
    public abstract Object clone() throws CloneNotSupportedException;

    @Override
    public abstract String toString();

    /**
     * Copy the members from another AbstractParameter
     *
     * @param rhs right hand side object to copy from
     */
    protected void copyMembers(AbstractParameter rhs)
    {
        mandatory = rhs.mandatory;
        value = rhs.value;
        defaultValue = rhs.defaultValue;
        fixed = rhs.fixed;
        optionallySelected = rhs.optionallySelected;
        multipleInstances = rhs.multipleInstances;
    }

    /**
     * Check whether the parameter requires an argument.
     *
     * @return true if so, false otherwise
     */
    public abstract boolean hasArgument();

    /**
     * Check whether the parameter argument is fixed (or can be changed).
     *
     * @return true if so, false otherwise
     */
    public final boolean hasFixedValue()
    {
        return value != null && !value.isEmpty() && fixed;
    }

    /**
     * Check whether can be used more than once.
     *
     * @return true if so, false otherwise
     */
    public final boolean multipleInstances()
    {
        return multipleInstances;
    }

    /**
     * Set that multiple instances of this parameter are allowed.
     */
    public final void allowMultipleInstances()
    {
        multipleInstances = true;
    }

    /**
     * Set that multiple instances of this parameter are disallowed.
     */
    public final void disallowMultipleInstances()
    {
        multipleInstances = false;
    }

    /**
     * Check whether the parameter argument has got a default.
     *
     * @return true if so, false otherwise
     */
    public final boolean hasDefaultedValue()
    {
        return defaultValue != null || !defaultValue.isEmpty();
    }

    /**
     * Make the parameter argument a fixed value.
     *
     * @param value a value that is not overridable by user, null or empty
     *              strings are setting the value to a double quote
     */
    public final void setFixedValue(String value)
    {
        this.value = (value == null || value.isEmpty()) ? "\"\"" : value;
        this.defaultValue = null;
        this.fixed = true;
    }

    /**
     * Make the parameter argument a changeable value.
     *
     * @param value
     */
    public void setCustomValue(String value)
    {
        this.value = value;
        this.fixed = false;
    }

    /**
     * Make the argument value changeable.
     */
    public final void unfixValue()
    {
        this.value = null;
        this.fixed = false;
    }

    /**
     * Set a default value for the parameter.
     *
     * @param defaultValue
     */
    public final void setDefault(String defaultValue)
    {
        // a default makes only sense if the value is not fixed
        this.value = null;
        this.defaultValue = defaultValue;
    }

    /**
     * Remove the default value for the parameter.
     */
    public final void undefaultValue()
    {
        this.defaultValue = null;
    }

    /**
     * Check whether the parameter is mandatory (or optional).
     *
     * @return true if so, false otherwise
     */
    public final boolean isMandatory()
    {
        return mandatory;
    }

    /**
     * Set whether or not the parameter is mandatory.
     *
     * @param mandatory
     */
    public final void setMandatory(boolean mandatory)
    {
        this.mandatory = mandatory;
    }

    /**
     * Retrieve the value of the argument.
     *
     * @return the value
     */
    public final String getValue()
    {
        return value == null ? "" : value;
    }

    public String getDefault()
    {
        return defaultValue == null ? "" : defaultValue;
    }

    public String getDefaultedValue()
    {
        return value != null && !value.isEmpty() ? value :
               defaultValue == null ? "" : defaultValue;
    }

    boolean isOptionallySelected()
    {
        return optionallySelected;
    }

    void selectAsUsed(boolean isSelected)
    {
        optionallySelected = isSelected;
    }

    boolean isUsed()
    {
        return isMandatory() || isOptionallySelected();
    }

}
