
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

/**
 * An interface formalising type constraints on columns in an Excel - sheet.
 *
 * @author kybelksd
 */
public interface ColumnTypeConstraints
{

    /**
     *
     * @return true if the type of the column is boolean, false otherwise
     */
    public boolean isBoolean();

    /**
     *
     * @return true if the type of the column is boolean, false otherwise
     */
    public boolean isInteger();

    /**
     *
     * @return true if the type of the column is Integer, false otherwise
     */
    public boolean isDouble();

    /**
     *
     * @return true if the type of the column is Double, false otherwise
     */
    public boolean isDate();

    /**
     *
     * @return true if the type of the column is Character, false otherwise
     */
    public boolean isChar();

    /**
     *
     * @return true if the type of the column is unsigned integer, false
     *         otherwise
     */
    public boolean isUInteger();

    /**
     *
     * @return true if the type of the column is unsigned double, false
     *         otherwise
     */
    public boolean isUDouble();

    /**
     *
     * @return true if the type of the column is String, false otherwise
     */
    public boolean isString();

    /**
     * Create an object of this class by scanning a string.
     *
     * @param str
     * @return the value that the String evaluates to
     */
    public ColumnTypeConstraints fromString(String str);

    /**
     *
     * @return the value that is treaded as the undefined value
     */
    public ColumnTypeConstraints undefinedType();

}
