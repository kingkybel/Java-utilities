
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

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Formalise additional information about columns in an Excel sheet.
 *
 * @author kybelksd
 */
public class ExcelColumnInfo
{

    private static final String CLASS_NAME = ExcelColumnInfo.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    ArrayList<Info> infos = new ArrayList<>();
    ColumnTypeConstraints validTypes;

    /**
     * Default construct.
     */
    public ExcelColumnInfo()
    {
    }

    /**
     * Construct a column info object with column headers.
     *
     * @param headers an array of column headers
     */
    public ExcelColumnInfo(Object[] headers)
    {
        set(headers);
    }

    /**
     * Construct a column info object with column headers and constraints on
     * these columns.
     *
     * @param headers   an array of column headers
     * @param cellTypes type constraints for cell values
     */
    public ExcelColumnInfo(Object[] headers, ColumnTypeConstraints[] cellTypes)
    {
        set(headers, cellTypes);
    }

    /**
     * Set column headers for this object..
     *
     * @param headers an array of column headers
     */
    public final void set(Object[] headers)
    {
        for (Object o : headers)
        {
            infos.add(new Info(o.toString()));
        }
    }

    /**
     * Set column headers for this object..
     *
     * @param headers   an array of column headers
     * @param cellTypes type constraints for cell values
     */
    public final void set(Object[] headers, ColumnTypeConstraints[] cellTypes)
    {
        int maxIndex = headers.length > cellTypes.length ?
                       headers.length :
                       cellTypes.length;
        for (int i = 0; i < maxIndex; i++)
        {
            String header = (i > headers.length) ? "" : headers[i].toString();
            ColumnTypeConstraints type = (i > cellTypes.length) ?
                                         null :
                                         cellTypes[i];
            infos.add(new Info(header, type));
        }
    }

    void set(int col)
    {
        for (int i = infos.size(); i < col; i++)
        {
            infos.add(new Info());
        }
        infos.get(col).setHeader("");
        infos.get(col).setType(null);
    }

    void set(int col, String header)
    {
        for (int i = infos.size(); i < col; i++)
        {
            infos.add(new Info());
        }
        infos.get(col).header = header;
        infos.get(col).type = null;

    }

    void set(int col, String header, ColumnTypeConstraints type)
    {
        for (int i = infos.size(); i < col; i++)
        {
            infos.add(new Info());
        }
        infos.get(col).header = header;
        infos.get(col).type = type;

    }

    /**
     * Retrieve the header of a column.
     *
     * @param col column index
     * @return column-name/header for the index, defaults to empty string if
     *         column-index is out of range
     */
    public String header(int col)
    {
        return col < 0 || col > infos.size() - 1 ? "" : infos.get(col).header();
    }

    /**
     * Retrieve the type of a column.
     *
     * @param col column index
     * @return the (meta-)type of the column, or the undefined value if the
     *         column-index is out of range
     * @throws Exception
     */
    public ColumnTypeConstraints type(int col) throws Exception
    {
        if (infos.isEmpty())
        {
            throw new Exception("ExcelColumnInfo: infos is empty()");
        }
        return col < 0 || col > infos.size() - 1 ?
               infos.get(0).type.undefinedType() : infos.get(col).type;
    }

    void append(String name, ColumnTypeConstraints nodeValueType)
    {
        if (infos == null)
        {
            infos = new ArrayList<>();
        }
        infos.add(new Info(name, nodeValueType));

    }

    /**
     * Helper construct to pair header and column types.
     */
    protected static class Info
    {

        private String header;
        private ColumnTypeConstraints type;

        /**
         * Default construct.
         */
        Info()
        {
            setHeader("");
            setType(null);
        }

        /**
         * Construct by just defining the header.
         *
         * @param header
         */
        Info(String header)
        {
            this();
            setHeader(header);
        }

        /**
         * Construct by defining the header and type.
         *
         * @param header
         * @param type
         */
        Info(String header, ColumnTypeConstraints type)
        {
            this(header);
            setType(type);
        }

        /**
         * Retrieve the header-name.
         *
         * @return the header-name, can be null
         */
        public String header()
        {
            return header;
        }

        /**
         * Retrieve the type.
         *
         * @return the meta-type, can be null
         */
        public ColumnTypeConstraints type()
        {
            return type;
        }

        private void setHeader(String header)
        {
            this.header = header == null ? "" : header;
        }

        private void setType(ColumnTypeConstraints type)
        {
            this.type = type;
        }
    }
}
