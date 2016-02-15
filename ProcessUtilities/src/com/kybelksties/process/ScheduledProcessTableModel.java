
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A table model extension for a collection of scheduled processes.
 *
 * @author kybelksd
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduledProcessTableModel extends AbstractTableModel
        implements Iterable<ScheduledProcess>,
                   Serializable,
                   ConcreteProcess.StateEventListener
{

    private static final String CLASS_NAME =
                                ScheduledProcessTableModel.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static final String[] theColumnNames =
    {
        "State",
        //        "ProcessType",
        "Executable",
        "WindowMode",
        "StartDirectory",
        "Parameters",
        "LogFile",
        "Sleep"//,
//        "BG",
//        "FG"
    };

    static String getLongColumnName(int col)
    {
        return theColumnNames[col];
    }
    private final ArrayList<ScheduledProcess> theScheduledProcesses =
                                              new ArrayList<>();

    /**
     * Default construct.
     */
    public ScheduledProcessTableModel()
    {
    }

    /**
     * Add a scheduled process.
     *
     * @param schedProc the new scheduled process to add to the model
     */
    public final void add(ScheduledProcess schedProc)
    {
        theScheduledProcesses.add(schedProc);
        int row = theScheduledProcesses.size() - 1;
        while (up(row))
        {
            row--;
        }
        while (down(row))
        {
            row++;
        }
        fireTableDataChanged();
    }

    /**
     * Reset the list clearing all scheduled processes.
     */
    public void reset()
    {
        theScheduledProcesses.clear();
        fireTableDataChanged();
    }

    /**
     * Set the list of scheduled processes.
     *
     * @param schedProcList a list of new scheduled processes
     */
    public void set(List<ScheduledProcess> schedProcList)
    {
        theScheduledProcesses.clear();
        Iterator<ScheduledProcess> iter = schedProcList.iterator();
        while (iter.hasNext())
        {
            ScheduledProcess info = iter.next();
            theScheduledProcesses.add(info);
        }
        fireTableDataChanged();
    }

    /**
     * Set the values of a row of the model.
     *
     * @param row         row index
     * @param modifiedDef new values from the scheduled process
     */
    public void setRow(int row, ScheduledProcess modifiedDef)
    {
        theScheduledProcesses.set(row, modifiedDef);
        fireTableDataChanged();
    }

    /**
     * Retrieve the scheduled process at table row by index.
     *
     * @param row the table row
     * @return the scheduled process
     */
    public ScheduledProcess get(int row)
    {
        return theScheduledProcesses.get(row);
    }

    /**
     * Remove the scheduled process at a specific row.
     *
     * @param row row index
     */
    public void removeRow(int row)
    {
        ScheduledProcess removed = theScheduledProcesses.remove(row);
        fireTableDataChanged();
    }

    /**
     * Remove the scheduled processes at a specified rows.
     *
     * @param rows an array of row-indices
     */
    public void removeRows(int[] rows)
    {
        for (int i = rows.length - 1; i >= 0; i--)
        {
            ScheduledProcess removed = theScheduledProcesses.remove(rows[i]);
        }
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int col)
    {
        return theColumnNames[col];
    }

    /**
     * Retrieve the table cell value in given row under given header.
     *
     * @param row          the selected row
     * @param columnHeader the column header as string
     * @return the cell value
     */
    public Object getValueAt(int row, String columnHeader)
    {
        int col = Arrays.asList(theColumnNames).indexOf(columnHeader);
        if (col == -1)
        {
            return 0;
        }
        return getValueAt(row, col);
    }

    @Override
    public synchronized Object getValueAt(int row, int col)
    {
        ScheduledProcess p = theScheduledProcesses.get(row);
        return (col == 0) ? p.getProcess() :
               //               (col == 1) ? p.getExeDefinition() :
               (col == 1) ? p.getExeDefinition().getExecutable() :
               (col == 2) ? p.getWindowMode() :
               (col == 3) ? p.getStartInDirectory() :
               (col == 4) ? p.getExeDefinition().getParameters().toString() :
               (col == 5) ? p.getLogFileName() :
               (col == 6) ? p.getNoopTime() :
               //               (col == 8) ? p.getWindowBackground() :
               //               (col == 9) ? p.getWindowForeground() :
               new Object();
    }

    @Override
    public void setValueAt(Object value, int row, int col)
    {
        ScheduledProcess newInfo = theScheduledProcesses.get(row);
        switch (col)
        {
            case 0:
                ConcreteProcess process = (ConcreteProcess) value;
                process.addStateChangeEventListener(this);
                newInfo.setProcess(process);
                break;
            case 1:
                newInfo.setExecutable((String) value);
                break;
            case 2:
                newInfo.setParameters((String) value);
                break;
            case 3:
                newInfo.setUsesXterm((WindowMode) value);
                break;
            case 4:
                newInfo.setStartInDirectory((String) value);
                break;
            case 5:
                newInfo.setLogFileName((String) value);
                break;
            case 6:
                newInfo.setNoopTime((Long) value);
                break;
//            case 8:
//                newInfo.setWindowBackground((String) value);
//                break;
//            case 9:
//                newInfo.setWindowForeground((String) value);
//                break;
        }
        theScheduledProcesses.set(row, newInfo);
        fireTableCellUpdated(row, col);
    }

    @Override
    public int getColumnCount()
    {
        return theColumnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return theScheduledProcesses.size();
    }

    @Override
    public Class<?> getColumnClass(int c)
    {
        if (theScheduledProcesses.isEmpty())
        {
            return Object.class;
        }
        Object obj = getValueAt(0, c);
        if (obj == null)
        {
            return Object.class;
        }
        return obj.getClass();
    }

    /**
     * Move the given row up one place in the table.
     *
     * @param row the given row index
     * @return true if the row has been moved, false otherwise (e.g. row '0'
     *         cannot be moved)
     */
    public boolean up(int row)
    {
        if (row < 1)
        {
            return false;
        }
        ScheduledProcess tmp = theScheduledProcesses.get(row);
        theScheduledProcesses.set(row, theScheduledProcesses.get(row - 1));
        theScheduledProcesses.set(row - 1, tmp);
        fireTableDataChanged();
        return true;
    }

    /**
     * Move the given row down one place in the table.
     *
     * @param row the given row index
     * @return true if the row has been moved, false otherwise (e.g. last row
     *         cannot be moved)
     */
    public boolean down(int row)
    {
        if (row > theScheduledProcesses.size() - 2)
        {
            return false;
        }
        ScheduledProcess tmp = theScheduledProcesses.get(row);
        theScheduledProcesses.set(row, theScheduledProcesses.get(row + 1));
        theScheduledProcesses.set(row + 1, tmp);
        fireTableDataChanged();
        return true;
    }

    @Override
    public Iterator<ScheduledProcess> iterator()
    {
        return theScheduledProcesses.iterator();
    }

    @Override
    public void processStateChanged(ConcreteProcess.StateEvent evt)
    {
        fireTableDataChanged();
    }

}
