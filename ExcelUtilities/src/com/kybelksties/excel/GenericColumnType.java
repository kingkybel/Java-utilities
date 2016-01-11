
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
package com.kybelksties.excel;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * A column type class as an all-purpose implementation of the
 * ColumnTypeConstraints interface.
 *
 * @author kybelksd
 */
public class GenericColumnType implements ColumnTypeConstraints,
                                          Comparable<GenericColumnType>
{

    private static final String UNDEF_STR = "<UNDEFINED>";
    private static final String CLASS_NAME = GenericColumnType.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private String typeString = UNDEF_STR;

    /**
     * Default construct.
     */
    public GenericColumnType()
    {
    }

    /**
     * Construct with column type.
     *
     * @param type string describing the type
     */
    public GenericColumnType(String type)
    {

        if (type == null || type.trim().isEmpty())
        {
            this.typeString = UNDEF_STR;
        }
        else
        {
            this.typeString = type.trim();
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.typeString);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final GenericColumnType other = (GenericColumnType) obj;
        return Objects.equals(this.typeString, other.typeString);
    }

    @Override
    public boolean isBoolean()
    {
        return false;
    }

    @Override
    public boolean isInteger()
    {
        return false;
    }

    @Override
    public boolean isDouble()
    {
        return false;
    }

    @Override
    public boolean isDate()
    {
        return false;
    }

    @Override
    public boolean isChar()
    {
        return false;
    }

    @Override
    public boolean isUInteger()
    {
        return false;
    }

    @Override
    public boolean isUDouble()
    {
        return false;
    }

    @Override
    public boolean isString()
    {
        return true;
    }

    @Override
    public ColumnTypeConstraints fromString(String str)
    {
        return new GenericColumnType(str);
    }

    @Override
    public ColumnTypeConstraints undefinedType()
    {
        return new GenericColumnType();
    }

    @Override
    public int compareTo(GenericColumnType rhs)
    {
        if (rhs == null)
        {
            return 1;
        }
        return typeString.compareToIgnoreCase(rhs.typeString);
    }

}
