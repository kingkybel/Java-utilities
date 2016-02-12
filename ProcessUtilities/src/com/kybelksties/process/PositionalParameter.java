
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Parameters that are interpreted according to the position in the argument
 * list as they are for example in shell-scripts.
 *
 * @author: Dieter J Kybelksties
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PositionalParameter extends AbstractParameter
{

    private static final String CLASS_NAME = PositionalParameter.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Default construct.
     */
    public PositionalParameter()
    {
        super();
    }

    /**
     * Construct with a value.
     *
     * @param value if value is not null the the parameter is constant
     */
    public PositionalParameter(String value)
    {
        super(value, null);
    }

    /**
     * Construct with a value and a default-value.
     *
     * @param value        if value is not null the the parameter is constant
     * @param defaultValue can be null, if value is null then this value will be
     *                     as default
     */
    public PositionalParameter(String value, String defaultValue)
    {
        super(value, defaultValue);
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        PositionalParameter reval = new PositionalParameter();
        reval.copyMembers(this);

        return reval;
    }

    @Override
    public String toString()
    {
        return getValue();
    }

    @Override
    public Boolean hasArgument()
    {
        return true;
    }

}
