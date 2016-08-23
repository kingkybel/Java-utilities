
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

import com.kybelksties.general.FileUtilities;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Wrapper to enable the display/editing of Excel/POI workbooks with GUI
 * components.
 *
 * @author kybelksd
 */
public class ExcelWorkbookTableModel extends AbstractTableModel
{

    private static final Class CLAZZ = ExcelWorkbookTableModel.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    Workbook workbook;
    int currentSheet;
    HashMap<Integer, ExcelSheetTableModel> sheet2Model = new HashMap<>();

    /**
     * Default construct.
     */
    public ExcelWorkbookTableModel()
    {
        setWorkbook(new XSSFWorkbook());
        addSheet();
    }

    /**
     * Construct by reading from a file.
     *
     * @param filename the file-path from which the workbook is read
     */
    public ExcelWorkbookTableModel(String filename)
    {
        this();
        try
        {
            read(filename);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add a sheet to the Workbook.
     *
     * @return the created sheet
     */
    public final Sheet addSheet()
    {
        return addSheet("");
    }

    /**
     * Add a named sheet to the Workbook.
     *
     * @param name the name appearing on the sheet tab
     * @return the created sheet
     */
    public final Sheet addSheet(String name)
    {
        Sheet sheet = getWorkbook().createSheet();
        currentSheet = workbook.getSheetIndex(sheet);
        if (name != null && !name.isEmpty())
        {
            getWorkbook().setSheetName(currentSheet, name);
        }
        workbook.setActiveSheet(currentSheet);
        try
        {
            sheet2Model.put(currentSheet,
                            new ExcelSheetTableModel(this, currentSheet));
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return sheet;
    }

    /**
     * Add a sheet from a table model to the Workbook.
     *
     * @param etm the excel sheet table model
     * @return the added sheet
     */
    public final Sheet addSheet(ExcelSheetTableModel etm)
    {
        return etm == null ? null : etm.getSheet();
    }

    /**
     * Retrieve the workbook.
     *
     * @return the workbook, can be null
     */
    public Workbook getWorkbook()
    {
        return workbook;
    }

    /**
     * Set the active sheet.
     *
     * @param sheetNumber
     */
    public void setSheet(int sheetNumber)
    {
        if (getWorkbook() != null)
        {
            currentSheet = sheetNumber;
        }
        getColumnCount();
        fireTableStructureChanged();
    }

    /**
     * Set the first sheet active.
     */
    public void setSheet()
    {
        setSheet(0);
    }

    /**
     * Set the POI workbook and add a sheet, if the workbook has none.
     *
     * @param workbook the workbook to add
     */
    protected final void setWorkbook(Workbook workbook)
    {
        this.workbook = workbook;
        int numSheets = getWorkbook().getNumberOfSheets();
        if (numSheets == 0)
        {
            addSheet();
        }
        ExcelSheetTableModel model = sheet2Model.get(currentSheet);
        model.fireTableStructureChanged();
    }

    @Override
    public int getRowCount()
    {
        ExcelSheetTableModel model = sheet2Model.get(currentSheet);
        return model.getRowCount();
    }

    @Override
    public int getColumnCount()
    {
        ExcelSheetTableModel model = sheet2Model.get(currentSheet);
        return model.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        ExcelSheetTableModel model = sheet2Model.get(currentSheet);
        return model.getValueAt(rowIndex, columnIndex);
    }

    /**
     * Set the Excel sheet table model for the workbook.
     *
     * @param model the new model
     */
    public void setSheetTableModel(ExcelSheetTableModel model)
    {
        if (model == null)
        {
            try
            {
                model = new ExcelSheetTableModel(this);
            }
            catch (Exception ex)
            {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        sheet2Model.put(currentSheet, model);
    }

    /**
     * Read a workbook from file.
     *
     * @param filename currently supported extensions are *.xls for classic
     *                 Excel, *.xlsx for current version and *.csv for comma
     *                 separated value files
     * @throws IOException thrown when file doesn't exist or cannot be read from
     */
    public final void read(String filename) throws IOException
    {
        try
        {
            String lwrFilename = filename.toLowerCase();
            try (FileInputStream fis = new FileInputStream(filename))
            {
                if (lwrFilename.endsWith(".xls"))
                {
                    setWorkbook(new XSSFWorkbook(fis));
                }
                else if (lwrFilename.endsWith(".xlsx"))
                {
                    setWorkbook(new HSSFWorkbook(fis));
                }
                else if (lwrFilename.endsWith(".csv"))
                {
                    setWorkbook(new XSSFWorkbook());
                    addSheet();
                    String contents = FileUtilities.readText(filename);
                    String[] lines = contents.split(
                             System.getProperty("line.separator"));
                    ExcelSheetTableModel model = sheet2Model.get(currentSheet);
                    int row = 0;
                    for (String line : lines)
                    {
                        String[] cellValues = line.split(",");
                        int col = 0;
                        for (String value : cellValues)
                        {
                            model.setValueAt(value.trim(), row, col);
                        }
                    }
                }

            }
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save the workbook in the current Excel-version.
     *
     * @param filename file to write to
     * @throws FileNotFoundException thrown when path to file doe not exist
     * @throws IOException           thrown when file cannot be written to
     */
    public void saveAs(String filename) throws FileNotFoundException,
                                               IOException
    {
        try (FileOutputStream out = new FileOutputStream(filename))
        {
            getWorkbook().write(out);
        }
    }
}
