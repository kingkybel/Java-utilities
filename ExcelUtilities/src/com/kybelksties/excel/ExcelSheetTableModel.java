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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.ErrorConstants;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.openide.util.NbBundle;

/**
 * Table model to make it easier to display Excel sheets in JTable objects.
 *
 * @author kybelksd
 */
public class ExcelSheetTableModel extends AbstractTableModel
{

    private static final Class CLAZZ = ExcelSheetTableModel.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Get the value contained in the cell.
     *
     * @param cell the examined cell
     * @return the value as Boolean, Numeric, String, Blank, Error or Formula
     */
    public static Object getCellValue(Cell cell)
    {
        return cell == null ? "" :
               cell.getCellType() == Cell.CELL_TYPE_BOOLEAN ?
               cell.getBooleanCellValue() :
               cell.getCellType() == Cell.CELL_TYPE_NUMERIC ?
               (DateUtil.isCellDateFormatted(cell) ?
                cell.getDateCellValue() :
                cell.getNumericCellValue()) :
               cell.getCellType() == Cell.CELL_TYPE_STRING ?
               cell.getStringCellValue() :
               cell.getCellType() == Cell.CELL_TYPE_BLANK ?
               cell.getStringCellValue() :
               cell.getCellType() == Cell.CELL_TYPE_ERROR ?
               cell.getErrorCellValue() :
               cell.getCellType() == Cell.CELL_TYPE_FORMULA ?
               cell.getCachedFormulaResultType() :
               cell.getStringCellValue();
    }
    private final Workbook workbook;
    private int currentSheet = 0;
    private int numberOfColumns = 0;

    /**
     * Construct using a workbook-model of the Excel object.
     *
     * @param workbookModel the workbook model
     * @throws Exception thrown when the passed in parameter is null
     */
    public ExcelSheetTableModel(ExcelWorkbookTableModel workbookModel) throws
            Exception
    {
        if (workbookModel == null)
        {
            throw new Exception(NbBundle.getMessage(
                    CLAZZ,
                    "ExcelSheetTableModel.nullWorkbookException"));
        }
        this.workbook = workbookModel.getWorkbook();
        Sheet sheet = this.workbook.createSheet();
        workbook.setActiveSheet(workbook.getSheetIndex(sheet));
    }

    /**
     * Construct using a workbook-model of the Excel object and a workbook
     * position in the Excel document.
     *
     * @param workbookModel the workbook model
     * @param sheetNum      the position of the sheet in the document
     * @throws Exception thrown when the passed in parameter is null
     */
    public ExcelSheetTableModel(ExcelWorkbookTableModel workbookModel,
                                int sheetNum)
            throws Exception
    {
        this(workbookModel);
        currentSheet = sheetNum;
    }

    /**
     * Construct using a workbook-model of the Excel object and a worksheet.
     *
     * @param workbookModel the workbook model
     * @param sheet         a sheet in the document
     * @throws Exception thrown when the passed in parameter is null
     */
    public ExcelSheetTableModel(ExcelWorkbookTableModel workbookModel,
                                Sheet sheet)
            throws Exception
    {
        this(workbookModel);
        currentSheet = workbook.getSheetIndex(sheet);
    }

    @Override
    public void fireTableStructureChanged()
    {
        super.fireTableStructureChanged();
        findNumberOfColumns();
    }

    /**
     * Retrieve the currently active sheet.
     *
     * @return the current Sheet
     */
    public Sheet getSheet()
    {
        return getSheet(currentSheet);
    }

    /**
     * Retrieve the sheet at the given index.
     *
     * @param sheetIndex index of the sheet in the workbook
     * @return the sheet, if index is in range, null else
     */
    public final Sheet getSheet(int sheetIndex)
    {
        Sheet reval = null;
        currentSheet = sheetIndex;
        if (workbook != null)
        {
            reval = workbook.getSheetAt(currentSheet);
            if (reval == null)
            {
                // we might need to add some sheets until we get the right number
                if (workbook.getNumberOfSheets() < sheetIndex)
                {
                    while (workbook.getNumberOfSheets() < sheetIndex)
                    {
                        workbook.createSheet();
                    }
                    currentSheet = sheetIndex;
                }
                reval = workbook.getSheetAt(currentSheet);
            }
        }
        return reval;
    }

    private void findNumberOfColumns()
    {
        numberOfColumns = 0;
        if (workbook != null)
        {
            Iterator<Row> iter = getSheet().rowIterator();
            while (iter.hasNext())
            {
                Row row = iter.next();
                // According to documentation, this method gets the index of
                // the last cell. This value is increased BY ONE, so for
                // example, with maximum index of 5 will be 6. I think this has
                // been done to simplify iteration over rowcell.
                int lastCol = row.getLastCellNum();
                if (numberOfColumns < lastCol)
                {
                    numberOfColumns = lastCol;
                }
            }
        }
    }

    @Override
    public int getRowCount()
    {
        if (getSheet().getPhysicalNumberOfRows() == 0)
        {
            return 0;
        }
        return getSheet().getLastRowNum() + 1;
    }

    @Override
    public int getColumnCount()
    {
        return numberOfColumns;
    }

    @Override
    public String getColumnName(int column)
    {
        int dividend = column + 1;
        String columnName = "";
        int modulo;

        while (dividend > 0)
        {
            modulo = (dividend - 1) % 26;
            columnName = (char) (65 + modulo) + columnName;
            dividend = ((dividend - modulo) / 26);
        }

        return columnName;

    }

    /**
     * Retrieve the index to a Excel-column name A1,A2,.... Might fail or be
     * incorrect for names that are not Excel-column-names.
     *
     * @param columnName the Excel type column name as string
     * @return the columnIndex
     */
    public int getColumnIndex(String columnName)
    {
        int result = 0;
        for (int i = 0; i < columnName.length(); i++)
        {
            if (columnName.charAt(i) < 'A' || columnName.charAt(i) > 'Z')
            {
                return 0;
            }
            result *= 26;
            result += columnName.charAt(i) - 'A' + 1;
        }
        return result - 1;
    }

    /**
     * Retrieve a cell at coordinates [rowIndex,colIndex].
     *
     * @param rowIndex row-number of the cell
     * @param colIndex column-number of the cell
     * @return the cell, if exists in the sheet, null otherwise
     */
    public Cell getCellAt(int rowIndex, int colIndex)
    {
        Row r = getSheet().getRow(rowIndex);
        Cell cell = null;
        if (r != null)
        {
            cell = r.getCell((short) colIndex);
        }
        return cell;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Cell cell = getCellAt(rowIndex, columnIndex);
        return getCellValue(cell);
    }

    /**
     * Retrieve the cell value as String.
     *
     * @param rowIndex row-number of the cell
     * @param colIndex column-number of the cell
     * @return the converted cell-value and trimmed cell value if exists, empty
     *         string else
     */
    public String getCellValueAsString(int rowIndex, int colIndex)
    {
        DataFormatter formatter = new DataFormatter();
        FormulaEvaluator eval = workbook.getCreationHelper().
                         createFormulaEvaluator();
        String value;
        Cell cell = getCellAt(rowIndex, colIndex);
        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
        {
            if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_ERROR)
            {
                value = ErrorConstants.getText(cell.getErrorCellValue());
            }
            else
            {
                value = formatter.formatCellValue(cell, eval);
            }
        }
        else
        {
            value = formatter.formatCellValue(cell);
        }
        return value.trim();
    }

    @Override
    public void setValueAt(Object obj, int rowIndex, int columnIndex)
    {
        Row row = getSheet().getRow(rowIndex);
        if (row == null)
        {
            row = getSheet().createRow(rowIndex);
        }
        Cell cell = row.getCell(columnIndex);
        if (cell == null)
        {
            cell = row.createCell(columnIndex);
        }

        if (obj != null)
        {
            Class<?> clazz = obj.getClass();
            if (String.class.isAssignableFrom(clazz))
            {
                cell.setCellValue(((String) obj));
            }
            else if (Number.class.isAssignableFrom(clazz))
            {
                cell.setCellValue(((Number) obj).doubleValue());
            }
            else if (Boolean.class.isAssignableFrom(clazz))
            {
                cell.setCellValue(((Boolean) obj));
            }
            else if (Calendar.class.isAssignableFrom(clazz))
            {
                cell.setCellValue(((Calendar) obj));
            }
            else if (Date.class.isAssignableFrom(clazz))
            {
                cell.setCellValue(((Date) obj));
            }
            else if (RichTextString.class.isAssignableFrom(clazz))
            {
                cell.setCellValue(((RichTextString) obj));
            }
            else
            {
                cell.setCellValue(obj.toString());
            }
        }

        if (columnIndex >= getColumnCount() || rowIndex >= getRowCount())
        {
            fireTableStructureChanged();
        }
        else
        {
            fireTableChanged(new TableModelEvent(this,
                                                 rowIndex,
                                                 rowIndex,
                                                 columnIndex));
        }
    }

    /**
     * Retrieve a row by index.
     *
     * @param rowIndex row-number of the cell
     * @return the row at the index, null if the row does not exist
     */
    public Row getRow(int rowIndex)
    {
        return getSheet().getRow(rowIndex);
    }

    /**
     * Retrieve a Column by index. Since Excel organises data as a set of rows
     * and doesn't have a column type, the method creates a list a list of cells
     * to return.
     *
     * @param columnIndex column-number of the cell
     * @return a list of cells in lieu of a column
     */
    public List<Cell> getColumn(int columnIndex)
    {
        List<Cell> reval = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++)
        {
            reval.add(getRow(rowIndex).getCell(columnIndex));
        }

        return reval;
    }

    /**
     * Insert a row at a given index.
     *
     * @param createAtIndex row-number of the cell at which to create a new row
     * @param sourceRow     the row to insert
     */
    public void insertRowAt(int createAtIndex, Row sourceRow)
    {
        Row newRow = getRow(createAtIndex);
        if (newRow != null)
        {
            // shift all rows >= createAtIndex up by one
            getSheet().shiftRows(createAtIndex, getSheet().getLastRowNum(), 1);
        }
        else
        {
            newRow = getSheet().createRow(createAtIndex);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++)
        {
            // Grab a copy of the old/new cell
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null)
            {
                continue;
            }

            // Copy style from old cell and apply to new cell
            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null)
            {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null)
            {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType())
            {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < getSheet().getNumMergedRegions(); i++)
        {
            CellRangeAddress cellRangeAddress = getSheet().getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum())
            {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(
                                 newRow.getRowNum(),
                                 (newRow.getRowNum() +
                                  (cellRangeAddress.getLastRow() -
                                   cellRangeAddress.getFirstRow())),
                                 cellRangeAddress.getFirstColumn(),
                                 cellRangeAddress.getLastColumn());
                getSheet().addMergedRegion(newCellRangeAddress);
            }
        }
    }

    /**
     * Append a row at the end.
     *
     * @param rowData a list of objects to populate the row with, can be size 0
     *                or contain null-elements
     */
    public void addRow(ArrayList<Object> rowData)
    {
        int newRowIndex = getRowCount();
        for (int columnIndex = 0; columnIndex < rowData.size(); columnIndex++)
        {
            setValueAt(rowData.get(columnIndex), newRowIndex, columnIndex);
        }
    }

    /**
     * TODO: Not implemented
     *
     * @param createAtIndex column-number of the cell at which to create a new
     *                      row
     * @param sourceColumn  a list of cells to populate the column with, can be
     *                      size 0 or contain null-elements
     */
    public void insertColumnAt(int createAtIndex, List<Cell> sourceColumn)
    {
        // a bit of a pain...
        // leave it for if and when it is needed. Involves moving contents
        // and particularly adjustment of any cell-references...
        String msg =
               "public void insertColumnAt(int createAtIndex, List<Cell> sourceColumn)";
        throw new UnsupportedOperationException(msg);
    }

    /**
     * Remove the row at given index.
     *
     * @param rowIndex the row-number of the row to delete
     */
    public void deleteRow(int rowIndex)
    {
        getSheet().removeRow(getSheet().getRow(rowIndex));
        fireTableStructureChanged();
    }

    /**
     * Remove the column at given index.
     *
     * @param columnIndex the column-number of the column to delete
     */
    public void deleteColumn(int columnIndex)
    {
        for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++)
        {
            Cell removableCell = getSheet().getRow(rowIndex).
                 getCell(columnIndex);
            getSheet().getRow(rowIndex).removeCell(removableCell);
        }
        fireTableStructureChanged();
    }

    /**
     * Append a column at the end of the sheet setting a default-value if it is
     * not null.
     *
     * @param defaultValue a value to fill the column with
     */
    public void addColumn(Object defaultValue)
    {
        numberOfColumns++;
        if (defaultValue != null)
        {
            int rowCount = getRowCount();
            for (int row = 0; row < rowCount; row++)
            {
                setValueAt(defaultValue, row, numberOfColumns - 1);
            }
        }
    }

    /**
     * Append a column at the end of the sheet.
     */
    public void addColumn()
    {
        addColumn(null);
    }

    @Override
    public String toString()
    {
        String reval = "";
        for (int row = 0; row < getRowCount(); row++)
        {
            for (int col = 0; col < getColumnCount(); col++)
            {
                Object val = getValueAt(row, col);
                reval += val == null || val.toString().isEmpty() ?
                         "''" :
                         val.toString() + "\t";
            }
            reval += System.getProperty("line.separator");
        }
        return reval;
    }

    /**
     * Write a comma separated file with contents of the current sheet's cells
     * converted to strings.
     *
     * @param writer         takes care of the writing of the CSV
     * @param delimiter      a delimiter to use between fields
     * @param columnHeadings include colum-headings if true, do not otherwise
     * @throws IOException thrown when write operation fails
     */
    public void exportCSV(Writer writer,
                          String delimiter,
                          boolean columnHeadings) throws IOException
    {
        if (columnHeadings)
        {
            for (int col = 0; col < getColumnCount(); col++)
            {
                String name = getColumnName(col);
                writer.write(name == null ? "" : name);
                if (col < getColumnCount() - 1)
                {
                    writer.write(delimiter);
                }
            }
            writer.write(System.getProperty("line.separator"));
        }
        for (int row = 0; row < getRowCount(); row++)
        {
            for (int col = 0; col < getColumnCount(); col++)
            {
                String value = getCellValueAsString(row, col);
                writer.write(value == null ? "" : value);
                if (col < getColumnCount() - 1)
                {
                    writer.write(delimiter);
                }
            }
            writer.write(System.getProperty("line.separator"));
        }
        writer.flush();
    }

}
