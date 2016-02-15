
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
import java.util.EventListener;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to hold a set of defined executables.
 *
 * @author kybelksd
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExeDefinitions
        extends AbstractTableModel
        implements Iterable<ExeDefinition>, Serializable
{

    private static final String CLASS_NAME = ExeDefinitions.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private int activeDefinition = -1;
    private ArrayList<ExeDefinition> defArray = new ArrayList<>();

    private final String[] theColumnNames =
    {
        "Name",
        "Executable",
        "Path",
        "Parameters",
    };

    /**
     * Default construct.
     */
    public ExeDefinitions()
    {
        defArray = new ArrayList<>();
        activeDefinition = -1;
    }

    /**
     * Copy construct.
     *
     * @param rhs right hand side object.
     */
    public ExeDefinitions(ExeDefinitions rhs)
    {
        defArray = new ArrayList<>(rhs.defArray);
        activeDefinition = rhs.activeDefinition;
    }

    @Override
    public <T extends EventListener> T[] getListeners(Class<T> listenerType)
    {
        return super.getListeners(listenerType);
    }

    @Override
    public Iterator<ExeDefinition> iterator()
    {
        return defArray.iterator();
    }

    int size()
    {
        return defArray.size();
    }

    /**
     * Retrieve all definitions.
     *
     * @return all definitions
     */
    public ArrayList<ExeDefinition> getDefinitions()
    {
        return defArray;
    }

    /**
     * Retrieve a definition identified by its name.
     *
     * @param name the identifier of the definition
     * @return the definition if one with the name exists, null otherwise
     */
    public ExeDefinition getDefinition(String name)
    {
        for (ExeDefinition def : defArray)
        {
            if (def.getName().equals(name))
            {
                return def;
            }
        }
        return null;
    }

    /**
     * Retrieve a definition identified by its index.
     *
     * @param index index in the collection, 0-based
     * @return the definition if one with the index exists, null otherwise
     */
    public ExeDefinition getDefinition(int index)
    {
        return (index > -1 && index < defArray.size()) ? defArray.get(index) :
               null;
    }

    /**
     * Append a new executable definition.
     *
     * @param newED the new executable definition
     */
    public void add(ExeDefinition newED)
    {
        defArray.add(newED);
        setActiveDefinition(defArray.size() - 1);
        fireTableDataChanged();

    }

    /**
     * Retrieve the definitions as array.
     *
     * @return the definitions
     */
    public ExeDefinition[] allValues()
    {
        return (ExeDefinition[]) getDefinitions().toArray();
    }

    /**
     * Get the definitions as ComboBoxModel for use in GUI population of
     * combo-boxes.
     *
     * @return the definitions wrapped in a combo-box-model
     */
    public DefaultComboBoxModel<ExeDefinition> getComboBoxModel()
    {
        return new DefaultComboBoxModel<>(allValues());
    }

    @Override
    public String getColumnName(int col)
    {
        return theColumnNames[col];
    }

    @Override
    public int getRowCount()
    {
        return defArray.size();
    }

    @Override
    public int getColumnCount()
    {
        return theColumnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        ExeDefinition exeDef = getDefinitions().get(row);
        return (col == 0) ? exeDef.getName() :
               (col == 1) ? exeDef.getExecutable() :
               (col == 2) ? exeDef.getPath() :
               (col == 3) ? exeDef.getParameters() :
               new Object();
    }

    @Override
    public void setValueAt(Object value, int row, int col)
    {
        ExeDefinition exeDef = getDefinitions().get(row);

        switch (col)
        {
            case 0:
//                exeDef.setName();
//                index2NameMap.set(row, (String) value);
                break;
            case 1:
                exeDef.setExecutable((String) value);
                break;
            case 2:
                exeDef.setPath((String) value);
                break;
            case 3:
                exeDef.setParameters((ParameterList) value);
                break;
        }
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int c)
    {
        if (getDefinitions().isEmpty())
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

    void remove(ExeDefinition exeDef)
    {
        defArray.remove(exeDef);
        fireTableDataChanged();
    }

    /**
     * Retrieve the currently active definition.
     *
     * @return the currently active definition
     */
    public ExeDefinition getActiveDefinition()
    {
        if (defArray == null || defArray.isEmpty())
        {
            return null;
        }
        if (activeDefinition > -1 && activeDefinition < defArray.size())
        {
            return defArray.get(activeDefinition);
        }
        return null;
    }

    /**
     * Set the currently active definition.
     *
     * @param index the new active definition
     */
    public void setActiveDefinition(Integer index)
    {
        this.activeDefinition = index > -1 && index < defArray.size() ? index :
                                -1;
    }

    /**
     * Remove all executable definitions from the collection.
     */
    public void clear()
    {
        defArray.clear();
        activeDefinition = -1;
        fireTableDataChanged();
    }

    boolean contains(ExeDefinition theExeDefinition)
    {
        return defArray.contains(theExeDefinition);
    }
}
