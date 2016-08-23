
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Table model for an extended excel sheet that contains column type
 * information.
 *
 * @author kybelksd
 */
public class SheetWithInfoTableModel extends ExcelSheetTableModel
{

    private static final Class CLAZZ = SheetWithInfoTableModel.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    TreeSet<ColumnTypeConstraints> validColumnTypes = new TreeSet<>();
    ExcelColumnInfo columnInfo = new ExcelColumnInfo();
    int originalTypeRowIndex = -1;
    int originalHeaderRowIndex = -1;
    Row typeRowBackup = null;
    Row headerRowBackup = null;

    /**
     * Create from a workbook table model.
     *
     * @param workbookModel the base workbook table model
     * @throws Exception thrown when the passed in workbookModel is null
     */
    public SheetWithInfoTableModel(ExcelWorkbookTableModel workbookModel)
            throws Exception
    {
        super(workbookModel);
    }

    /**
     * Create from a workbook table model and column types.
     *
     * @param workbookModel the base workbook table model
     * @param types         a set of valid types implementing from interface
     *                      ColumnTypeConstraints.
     * @throws Exception thrown when the passed in workbookModel is null
     */
    public SheetWithInfoTableModel(ExcelWorkbookTableModel workbookModel,
                                   ColumnTypeConstraints[] types)
            throws Exception
    {
        super(workbookModel);
        setValidColumnTypes(types);
    }

    /**
     * Construct from a workbook-model, sheet-number and constraints.
     *
     * @param workbookModel the base workbook table model
     * @param sheetNum      sheet index in the workbook
     * @param types         a set of valid types implementing from interface
     *                      ColumnTypeConstraints.
     * @throws Exception thrown when the passed in workbookModel is null
     */
    public SheetWithInfoTableModel(ExcelWorkbookTableModel workbookModel,
                                   int sheetNum,
                                   ColumnTypeConstraints[] types)
            throws Exception
    {
        super(workbookModel, sheetNum);
        setValidColumnTypes(types);
    }

    /**
     * Construct from a workbook-model, sheet and constraints.
     *
     * @param workbookModel the base workbook table model
     * @param sheet         sheet in the workbook
     * @param types         a set of valid types implementing from interface
     *                      ColumnTypeConstraints.
     * @throws Exception thrown when the passed in workbookModel is null
     */
    public SheetWithInfoTableModel(ExcelWorkbookTableModel workbookModel,
                                   Sheet sheet,
                                   ColumnTypeConstraints[] types)
            throws Exception
    {
        super(workbookModel, sheet);
        setValidColumnTypes(types);
    }

    @Override
    public String toString()
    {
        String reval = "";
        for (ExcelColumnInfo.Info info : columnInfo.infos)
        {
            reval += info.header() + "[" + info.type() + "]\t";
        }
        reval += "\n";
        reval += super.toString();
        return reval;
    }

    @Override
    public String getColumnName(int column)
    {
        columnInfo.header(column);
        return columnInfo.header(column);
    }

    /**
     * Retrieve the (meta-)value-type of a given column.
     *
     * @param col the given column
     * @return the value-type if column-info is available, null otherwise
     */
    public ColumnTypeConstraints getValueType(int col)
    {
        ColumnTypeConstraints reval = null;
        try
        {
            reval = columnInfo.type(col);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return reval;
    }

    /**
     * Set all the valid (meta-)column-types.
     *
     * @param types a set of valid types implementing from interface
     *              ColumnTypeConstraints.
     */
    public final void setValidColumnTypes(ColumnTypeConstraints[] types)
    {
        if (types != null)
        {
            setValidColumnTypes(Arrays.asList(types));
        }
    }

    /**
     * Set all the valid (meta-)column-types.
     *
     * @param types a set of valid types implementing from interface
     *              ColumnTypeConstraints.
     */
    public final void setValidColumnTypes(
            Collection<ColumnTypeConstraints> types)
    {
        validColumnTypes.clear();
        if (types != null)
        {
            validColumnTypes.addAll(types);
        }
    }

    /**
     * Treat one of the rows as header rows and one of the rows as type row.
     * This effectively removes the two rows from the spreadsheet and creates a
     * meta-info for the sheet. The removed rows are backed up so that they can
     * be re-instated if required.
     *
     * @param headerRow the row to be treated as header.
     */
    public final void setHeaderRow(int headerRow)
    {
        if (headerRow < 0)
        {
            return;
        }
        int colCount = getColumnCount();
        for (int col = 0; col < colCount; col++)
        {
            String headerStr = getCellValueAsString(headerRow, col);
            columnInfo.set(col, headerStr);
        }
        restoreHeaderRow();
        originalHeaderRowIndex = headerRow;
        headerRowBackup = getRow(originalHeaderRowIndex);

        fireTableStructureChanged();

    }

    /**
     * Restore the currently backed up row if there is one.
     */
    public final void restoreHeaderRow()
    {
        if (originalHeaderRowIndex > -1)
        {
            insertRowAt(originalHeaderRowIndex, headerRowBackup);
            originalHeaderRowIndex = -1;
            headerRowBackup = null;
        }

    }

    /**
     * Restore the currently backed up row if there is one.
     */
    public final void restoreTypeRow()
    {
        if (originalTypeRowIndex > -1)
        {
            insertRowAt(originalTypeRowIndex, typeRowBackup);
            originalTypeRowIndex = -1;
            typeRowBackup = null;
        }
    }

    /**
     * Treat one of the rows as header rows and one of the rows as type row.
     * This effectively removes the two rows from the spreadsheet and creates a
     * meta-info for the sheet. The removed rows are backed up so that they can
     * be re-instated if required.
     *
     * @param typeRow the row to be treated as type.
     * @param types   a set of valid types implementing from interface
     *                ColumnTypeConstraints.
     */
    public final void setTypeRow(int typeRow, ColumnTypeConstraints[] types)
    {
        // if we pass a new set cof column constraints then set them
        if (types != null)
        {
            setValidColumnTypes(types);
        }
        int colCount = getColumnCount();
        // if the valid column types are empty, then create generic columnTypes
        if (validColumnTypes == null || validColumnTypes.isEmpty())
        {
            validColumnTypes = new TreeSet<>();
            for (int col = 0; col < colCount; col++)
            {
                String typeStr = getCellValueAsString(typeRow, col);
                GenericColumnType ctc;
                ctc = new GenericColumnType(typeStr);
                validColumnTypes.add(ctc);
            }

        }

        for (int col = 0; col < colCount; col++)
        {
            String typeStr = getCellValueAsString(typeRow, col);

            // dummy needs to be there because ColumnTypeConstraints is an
            // interface and has no static methods.
            ColumnTypeConstraints dummy = validColumnTypes.iterator().next();
            ColumnTypeConstraints type = dummy.fromString(typeStr);
            columnInfo.set(col, columnInfo.header(col), type);
        }
        restoreTypeRow();
        originalTypeRowIndex = typeRow;
        typeRowBackup = getRow(originalTypeRowIndex);

        fireTableStructureChanged();

    }

    /**
     * Append a "virtual" column by appending column-name and type.
     *
     * @param name name of the column/header.
     * @param info type constraint info
     */
    public void addColumnInfo(String name, ColumnTypeConstraints info)
    {
        if (columnInfo == null)
        {
            columnInfo = new ExcelColumnInfo();
        }
        columnInfo.append(name, info);
    }

    /**
     * Append a column at the end of the sheet including the meta-info given.
     *
     * @param header       column header
     * @param info         type constraint info
     * @param defaultValue a value conforming to the type constraint that will
     *                     be used as default in any cell of the column if not
     *                     otherwise defined
     */
    public void addColumn(String header,
                          ColumnTypeConstraints info,
                          Object defaultValue)
    {
        super.addColumn(defaultValue);
        columnInfo.append(header, info);
    }

    /**
     * Retrieve the value at given coordinates as Boolean value.
     *
     * @param rowIndex row index of the queried cell
     * @param colIndex column index of the queried cell
     * @return the value or null if the value is not boolean
     */
    public Boolean getBool(int rowIndex, int colIndex)
    {
        return getValueType(colIndex).isBoolean() ?
               (Boolean) getValueAt(rowIndex, colIndex) :
               null;
    }

    /**
     * Retrieve the value at given coordinates as character value.
     *
     * @param rowIndex row index of the queried cell
     * @param colIndex column index of the queried cell
     * @return the value or null if the value is not character
     */
    public Character getChar(int rowIndex, int colIndex)
    {
        return getValueType(colIndex).isChar() ?
               getValueAt(rowIndex, colIndex).toString().charAt(0) :
               null;
    }

    /**
     * Retrieve the value at given coordinates as integer value.
     *
     * @param rowIndex row index of the queried cell
     * @param colIndex column index of the queried cell
     * @return the value or null if the value is not integer
     */
    public Integer getInt(int rowIndex, int colIndex)
    {
        ColumnTypeConstraints type = getValueType(colIndex);
        return type.isInteger() || type.isUInteger() ?
               (Integer) getValueAt(rowIndex, colIndex) :
               null;
    }

    /**
     * Retrieve the value at given coordinates as unsigned integer value.
     *
     * @param rowIndex row index of the queried cell
     * @param colIndex column index of the queried cell
     * @return the value or null if the value is not unsigned integer
     */
    public Integer getUInt(int rowIndex, int colIndex)
    {
        return getValueType(colIndex).isUInteger() ?
               (Integer) getValueAt(rowIndex, colIndex) :
               null;
    }

    /**
     * Retrieve the value at given coordinates as floating point value.
     *
     * @param rowIndex row index of the queried cell
     * @param colIndex column index of the queried cell
     * @return the value or null if the value is not a float
     */
    public Double getFloat(int rowIndex, int colIndex)
    {
        ColumnTypeConstraints type = getValueType(colIndex);
        return type.isDouble() || type.isUDouble() ?
               (Double) getValueAt(rowIndex, colIndex) :
               null;
    }

    /**
     * Retrieve the value at given coordinates as String value.
     *
     * @param rowIndex row index of the queried cell
     * @param colIndex column index of the queried cell
     * @return the value or an empty String if the value is not string
     */
    public String getString(int rowIndex, int colIndex)
    {
        return getValueAt(rowIndex, colIndex).toString().trim();
    }

    /**
     * Retrieve the value at given coordinates as Date value.
     *
     * @param rowIndex row index of the queried cell
     * @param colIndex column index of the queried cell
     * @return the value or null if the value is not a date
     */
    public Date getDate(int rowIndex, int colIndex)
    {
        return getValueType(colIndex).isDate() ?
               (Date) getValueAt(rowIndex, colIndex) :
               null;
    }

}
