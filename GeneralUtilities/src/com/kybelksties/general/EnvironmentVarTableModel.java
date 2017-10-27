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
package com.kybelksties.general;

import com.kybelksties.general.EnvironmentVar.StereoType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.openide.util.NbBundle;

/**
 * A table model for categorized variables. Helps display in GUI components such
 * as JTable.
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EnvironmentVarTableModel
        extends AbstractTableModel
        implements Iterable<String>
{

    private static final Class CLAZZ = EnvironmentVarTableModel.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private final String[] theColumnNames =
    {
        NbBundle.getMessage(CLAZZ, "EnvironmentVarTableModel.header.defUndef"),
        NbBundle.getMessage(CLAZZ, "EnvironmentVarTableModel.header.variable"),
        NbBundle.getMessage(CLAZZ, "EnvironmentVarTableModel.header.type"),
        NbBundle.getMessage(CLAZZ, "EnvironmentVarTableModel.header.value")
    };

    private final ArrayList<EnvironmentVar> theEnvironmentVarInfos =
                                            new ArrayList<>();
    private final Map<Integer, String> index2name = new HashMap<>();
    private final Map<String, Integer> name2index = new HashMap<>();

    /**
     * Default construct.
     */
    public EnvironmentVarTableModel()
    {
        super();
    }

    /**
     * Copy construct.
     *
     * @param rhs right hand side
     */
    public EnvironmentVarTableModel(EnvironmentVarTableModel rhs)
    {
        this();
        for (EnvironmentVar rhsVar : rhs.theEnvironmentVarInfos)
        {
            add(new EnvironmentVar(rhsVar));
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex != 1;
    }

    /**
     * Undefine all environment variables.
     */
    public void undefineAll()
    {
        for (EnvironmentVar envVarInfo : theEnvironmentVarInfos)
        {
            envVarInfo.setDefined(false);
        }
        fireTableDataChanged();
    }

    /**
     * Make all environment variables "defined".
     */
    public void defineAll()
    {
        for (EnvironmentVar envVarInfo : theEnvironmentVarInfos)
        {
            envVarInfo.setDefined(true);
        }
        fireTableDataChanged();
    }

    @Override
    public String toString()
    {
        String reval = "{" + StringUtils.NEWLINE;
        for (EnvironmentVar envVarInfo : theEnvironmentVarInfos)
        {
            reval += StringUtils.NEWLINE +
                     "{ \"" +
                     envVarInfo.getStereoType() +
                     "\",\"" +
                     envVarInfo.getName() +
                     "\",\"" +
                     envVarInfo.getValue().toString() +
                     "\" }," +
                     StringUtils.NEWLINE;
        }
        reval += "}" + StringUtils.NEWLINE;
        return reval;
    }

    /**
     * Check whether a variable is contained in the model.
     *
     * @param name variable name
     * @return true if so, false otherwise
     */
    public boolean contains(String name)
    {
        return index2name.values().contains(name);
    }

    /**
     * Reset the table model by clearing all the values and informing listeners.
     */
    public void reset()
    {
        theEnvironmentVarInfos.clear();
        index2name.clear();
        name2index.clear();
        fireTableDataChanged();
    }

    final void add(EnvironmentVar var)
    {
        Integer row = theEnvironmentVarInfos.size();
        // if the variable already exists then replace it
        if (contains(var.getName()))
        {
            row = name2index.get(var.getName());
            theEnvironmentVarInfos.set(row, var);
        }
        else
        {
            theEnvironmentVarInfos.add(var);
            index2name.put(row, var.getName());
            name2index.put(var.getName(), row);
        }
        fireTableRowsUpdated(row, row);
    }

    /**
     * Sets all boolean variables to the same value.
     *
     * @param on true or false
     */
    public void setBooleans(boolean on)
    {
        for (EnvironmentVar var : theEnvironmentVarInfos)
        {
            if (var.getValue().isBoolean())
            {
                var.getValue().setBooleanValue(on);
            }
        }
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int col)
    {
        return theColumnNames[col];
    }

    @Override
    public synchronized Object getValueAt(int row, int col)
    {
        PodVariant rowValue = get(row).getValue();
        return (col == 0) ? get(row).getDefined() :
               (col == 1) ? index2name.get(row) :
               (col == 2) ? get(row).getStereoType() :
               (col == 3) ? rowValue.getValue() :
               null;
    }

    @Override
    public void setValueAt(Object value, int row, int col)
    {
        if (row >= theEnvironmentVarInfos.size())
        {
            for (int i = theEnvironmentVarInfos.size(); i < row; i++)
            {
                theEnvironmentVarInfos.add(new EnvironmentVar());
            }
        }
        EnvironmentVar currentInfo = theEnvironmentVarInfos.get(row);

        switch (col)
        {
            case 0:
                currentInfo.setDefined((Boolean) value);
                break;
            case 2:
                StereoType stereoType = (StereoType) value;
                //         PodVariant.Type type = stereoType.getPodVariantType();
                currentInfo.setStereoType(stereoType);
                //         currentInfo.setValue(new PodVariant(type));
                break;
            case 3:
                currentInfo.setValue(new PodVariant(value));
                break;
        }
        theEnvironmentVarInfos.set(row, currentInfo);
        fireTableRowsUpdated(row, row);
    }

    /**
     * Retrieve the environment variable in specified row.
     *
     * @param row the row index
     * @return the variable
     */
    public EnvironmentVar get(int row)
    {
        return theEnvironmentVarInfos.get(row);
    }

    /**
     * Get a variable by name.
     *
     * @param name the name of the variable
     * @return the variable
     */
    public EnvironmentVar get(String name)
    {
        Integer row = name2index.get(name);
        return row != null ? theEnvironmentVarInfos.get(row) : null;
    }

    @Override
    public int getColumnCount()
    {
        return theColumnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return theEnvironmentVarInfos.size();
    }

    @Override
    public Class<?> getColumnClass(int c)
    {
        if (theEnvironmentVarInfos.isEmpty())
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

    @Override
    public Iterator<String> iterator()
    {
        return name2index.keySet().iterator();
    }
}
