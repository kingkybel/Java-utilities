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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import org.openide.util.NbBundle;

/**
 * A list of AbstractParameters for a command.
 *
 * @author: Dieter J Kybelksties
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterList
        extends AbstractTableModel
        implements Iterable<AbstractParameter>, Serializable
{

    private static final Class CLAZZ = ParameterList.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private static final long serialVersionUID = -8940196742313991701L;

    private ModelMode theModelMode;

    private final String[] theColumnNamesPos =
    {
        NbBundle.getMessage(CLAZZ, "ParameterList.column.position"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.mandatory"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.value"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.default")
    };

    private final String[] theColumnNamesLet =
    {
        NbBundle.getMessage(CLAZZ, "ParameterList.column.parameter"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.hasArgument"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.mandatory"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.value"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.default")
    };

    private final String[] thePosEvalColumnNames =
    {
        NbBundle.getMessage(CLAZZ, "ParameterList.column.useParameter"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.positionalArgument")
    };

    private final String[] theParamLettEvalColumnNames =
    {
        NbBundle.getMessage(CLAZZ, "ParameterList.column.useParameter"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.parameterLetter"),
        NbBundle.getMessage(CLAZZ, "ParameterList.column.argument")
    };

    Integer lastMandatoryPosition = -1;

    @XmlElementWrapper(name = "specifiedParameters")
    ArrayList<AbstractParameter> parameterArray = new ArrayList<>();

    boolean isPositional;

    /**
     * This is needed to send messages to non-table Objects that listen to
     * changes on this table model.
     */
    protected EventListenerList nonTableListenerList = new EventListenerList();

    /**
     * Copy constructor.
     *
     * @param rhs
     */
    public ParameterList(ParameterList rhs)
    {
        this.theModelMode = rhs.theModelMode;
        this.isPositional = rhs.isPositional;
        for (AbstractParameter pc : rhs.parameterArray)
        {
            try
            {
                add((AbstractParameter) pc.clone());
            }
            catch (CloneNotSupportedException ex)
            {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        fireTableDataChanged();
    }

    /**
     * Default construct.
     */
    public ParameterList()
    {
        isPositional = false;
        theModelMode = ModelMode.EDIT;
    }

    private void updateParam(int row, AbstractParameter newInfo)
    {
        while (parameterArray.size() <= row)
        {
            parameterArray.add(newInfo);
        }
        fireTableDataChanged();
    }

    void setModelMode(ModelMode modelMode)
    {
        theModelMode = modelMode;
    }

    List<String> getAsStringList()
    {
        List<String> reval = new ArrayList<>();
        for (AbstractParameter row : parameterArray)
        {
            if (row.isUsed())
            {
                reval.add(row.toString());
            }
        }
        return reval;
    }

    boolean requiresValue(int row)
    {
        return parameterArray.get(row).isMandatory() ||
               (isPositional && row <= getLastMandatoryPosition());
    }

    boolean allowsValue(int row)
    {
        return !parameterArray.get(row).hasFixedValue();
    }

    boolean hasArgument(int row)
    {
        return parameterArray.get(row).hasArgument();
    }

    int size()
    {
        return parameterArray.size();
    }

    boolean contains(String paramName)
    {
        if (paramName == null || isPositional)
        {
            return false;
        }
        for (AbstractParameter param : parameterArray)
        {
            if (param.getClass() == LetterParameter.class)
            {
                if (((LetterParameter) param).letterEquals(paramName.charAt(0)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    void remove(String paramName)
    {
        int i = 0;
        for (AbstractParameter param : parameterArray)
        {
            if (param.getClass() == LetterParameter.class)
            {
                if (((LetterParameter) param).letterEquals(paramName.charAt(0)))
                {
                    removeRow(i);
                    return;
                }
            }
            i++;
        }
    }

    void addPositionalParam()
    {
        add(new PositionalParameter());
    }

    boolean isUsed(int row)
    {
        return parameterArray.get(row).isUsed();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        if (theModelMode == ModelMode.EDIT)
        {
            if (isPositional)
            {
                return (columnIndex != 0); // position (spelled out) cannot be edited
            }
            else
            {
                return true; // all can be edited
            }
        }
        else
        {
            if (theModelMode == ModelMode.PARAMLETTER_EVALUATED)
            {
                if (columnIndex == 0)
                {
                    return !parameterArray.get(rowIndex).isMandatory();
                }
                if (columnIndex == 1)
                {
                    return false;
                }
                return hasArgument(rowIndex);
            }
            else if (theModelMode == ModelMode.POSITIONAL_EVALUATED)
            {
                if (columnIndex == 0)
                {
                    return !parameterArray.get(rowIndex).isMandatory();
                }
                return true;
            }
        }
        return false;
    }

    private void renumber()
    {
        int i = 0;
        lastMandatoryPosition = -1;
        for (AbstractParameter param : parameterArray)
        {
            updateParam(i, param);
            if (param.isMandatory())
            {
                lastMandatoryPosition = i;
            }
            i++;
        }
    }

    boolean up(int row)
    {
        if (row < 1)
        {
            return false;
        }
        AbstractParameter tmp = parameterArray.get(row);
        parameterArray.set(row, (AbstractParameter) parameterArray.get(row - 1));
        parameterArray.set(row - 1, tmp);
        renumber();
        fireTableDataChanged();

        return true;
    }

    boolean down(int row)
    {
        if (row > parameterArray.size() - 2)
        {
            return false;
        }
        AbstractParameter tmp = parameterArray.get(row);
        parameterArray.set(row, (AbstractParameter) parameterArray.get(row + 1));
        parameterArray.set(row + 1, tmp);
        renumber();
        fireTableDataChanged();

        return true;
    }

    /**
     * Remove all parameters.
     */
    public void clear()
    {
        parameterArray.clear();
        fireTableDataChanged();
    }

    /**
     * Remove parameter with index row.
     *
     * @param row the the table row to remove
     */
    public void removeRow(int row)
    {
        parameterArray.remove(row);
        renumber();
        fireTableDataChanged();
    }

    /**
     * Remove all rows in the array of indices given.
     *
     * @param rows array of rows to remove
     */
    public void removeRows(int[] rows)
    {
        for (int i = 0; i < rows.length; i++)
        {
            parameterArray.remove(rows[i]);
        }
        renumber();
        fireTableDataChanged();
    }

    @Override
    public Iterator<AbstractParameter> iterator()
    {
        return parameterArray.iterator();
    }

    /**
     * Get the position of the last mandatory parameter. All parameters at
     * higher positions are optional.
     *
     * @return the highest required index
     */
    public Integer getLastMandatoryPosition()
    {
        return lastMandatoryPosition;
    }

    /**
     * Get the number mandatory parameters.
     *
     * @return the number mandatory parameters
     */
    public Integer getNumberMandatoryParams()
    {
        return getLastMandatoryPosition() + 1;
    }

    /**
     * Add a listener to receive update messages.
     *
     * @param listener the object to receive update messages
     */
    public void addTableUpdatedEventListener(
            ParamTableUpdatedEventListener listener)
    {
        nonTableListenerList.add(ParamTableUpdatedEventListener.class, listener);
    }

    /**
     * Remove a listener from receiving update messages.
     *
     * @param listener
     */
    public void removeMyEventListener(
            ParamTableUpdatedEventListener listener)
    {
        nonTableListenerList.remove(ParamTableUpdatedEventListener.class,
                                    listener);
    }

    void fireTableUpdatedEvent()
    {
        fireTableUpdatedEvent(new ParamTableUpdatedEvent(this));
    }

    void fireTableUpdatedEvent(ParamTableUpdatedEvent evt)
    {
        Object[] listeners = nonTableListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2)
        {
            if (listeners[i] == ParamTableUpdatedEventListener.class)
            {
                ((ParamTableUpdatedEventListener) listeners[i + 1]).
                        tableUpdatedEventOccurred(
                                evt);
            }
        }
    }

    /**
     * Add a parameter to the list.
     *
     * @param param the new parameter
     */
    public final void add(AbstractParameter param)
    {
        parameterArray.add(param);
        fireTableStructureChanged();
        fireTableUpdatedEvent();
    }

    @Override
    public String toString()
    {
        String reval = "";
        for (AbstractParameter p : parameterArray)
        {
            if (p.isUsed())
            {
                reval += p.toString() + " ";
            }
        }
        return reval;
    }

    /**
     * Add all parameters by scanning the string.
     *
     * @param paramString a string containing white-space separated parameters
     */
    public void fromString(String paramString)
    {
        clear();
        String[] ar = paramString.split(" ");
        Integer incr = isPositional ? 1 : 2;
        for (Integer i = 0; i < ar.length; i += incr)
        {
            if (isPositional)
            {
                add(new PositionalParameter(ar[i]));
            }
            else
            {
                add(new LetterParameter(ar[i].charAt(0), ar[i + 1]));
            }
        }
    }

    @Override
    public String getColumnName(int column)
    {
        return theModelMode == ModelMode.EDIT ? (isPositional ?
                                                 theColumnNamesPos[column] :
                                                 theColumnNamesLet[column]) :
               theModelMode == ModelMode.POSITIONAL_EVALUATED ?
               thePosEvalColumnNames[column] :
               theModelMode == ModelMode.PARAMLETTER_EVALUATED ?
               theParamLettEvalColumnNames[column] :
               "";
    }

    @Override
    public int getRowCount()
    {
        return parameterArray.size();
    }

    @Override
    public int getColumnCount()
    {
        return theModelMode == ModelMode.EDIT ? (isPositional ?
                                                 theColumnNamesPos.length :
                                                 theColumnNamesLet.length) :
               theModelMode == ModelMode.POSITIONAL_EVALUATED ?
               thePosEvalColumnNames.length :
               theModelMode == ModelMode.PARAMLETTER_EVALUATED ?
               theParamLettEvalColumnNames.length :
               0;
    }

    /**
     * Make the parameter-list a positional/non-positional one.
     *
     * @param isPositional true: positional, false: non-positional
     */
    public void makePositional(boolean isPositional)
    {
        this.isPositional = isPositional;
        parameterArray.clear();
        fireTableStructureChanged();
        fireTableUpdatedEvent();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (theModelMode == ModelMode.EDIT)
        {
            if (isPositional)
            {
                PositionalParameter param =
                                    (PositionalParameter) parameterArray.get(
                                            rowIndex);
                return (columnIndex == 0) ? rowIndex :
                       (columnIndex == 1) ? param.isMandatory() :
                       (columnIndex == 2) ? param.getValue() :
                       (columnIndex == 3) ? param.getDefault() :
                       new Object();
            }
            else
            {
                LetterParameter param = (LetterParameter) parameterArray.
                                get(rowIndex);
                return (columnIndex == 0) ? param.getLetter() :
                       (columnIndex == 1) ? param.hasArgument() :
                       (columnIndex == 2) ? param.isMandatory() :
                       (columnIndex == 3) ? param.getValue() :
                       (columnIndex == 4) ? param.getDefault() :
                       new Object();
            }
        }
        else if (theModelMode == ModelMode.POSITIONAL_EVALUATED)
        {
            PositionalParameter param = (PositionalParameter) parameterArray.
                                get(
                                        rowIndex);
            return (columnIndex == 0) ? param.isUsed() :
                   (columnIndex == 1) ? param.getDefaultedValue() :
                   new Object();
        }
        else
        {
            LetterParameter param = (LetterParameter) parameterArray.get(
                            rowIndex);
            return (columnIndex == 0) ? param.isUsed() :
                   (columnIndex == 1) ? param.getLetter() :
                   (columnIndex == 2) ? param.getDefaultedValue() :
                   new Object();
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if (theModelMode == ModelMode.EDIT)
        {
            if (isPositional)
            {
                PositionalParameter param =
                                    (PositionalParameter) parameterArray.get(
                                            rowIndex);
                PositionalParameter newInfo = param; // copy

                switch (columnIndex)
                {
                    case 0:
                        // should be just the row number
                        break;
                    case 1:
                        newInfo.setMandatory((boolean) aValue);
                        break;
                    case 2:
                        newInfo.setFixedValue((String) aValue);
                        break;
                    case 3:
                        newInfo.setDefault((String) aValue);
                        break;
                }
                updateParam(rowIndex, newInfo);
            }
            else
            {
                LetterParameter param = (LetterParameter) parameterArray.
                                get(rowIndex);
                LetterParameter newInfo = param; // copy

                switch (columnIndex)
                {
                    case 0:
                        newInfo.setLetter((Character) aValue);
                        break;
                    case 1:
                        newInfo.addFixedArgument(null);
                        break;
                    case 2:
                        newInfo.setMandatory((boolean) aValue);
                        break;
                    case 3:
                        newInfo.setFixedValue((String) aValue);
                        break;
                    case 4:
                        newInfo.setDefault((String) aValue);
                        break;
                }
                updateParam(rowIndex, newInfo);
            }
        }
        else if (theModelMode == ModelMode.POSITIONAL_EVALUATED)
        {
            PositionalParameter p = (PositionalParameter) parameterArray.
                                get(rowIndex);
            switch (columnIndex)
            {
                case 0:
                    p.selectAsUsed((boolean) aValue);
                    break;
                case 1:
                    p.setCustomValue((String) aValue);
                    break;
            }
        }
        else if (theModelMode == ModelMode.PARAMLETTER_EVALUATED)
        {
            LetterParameter p = (LetterParameter) parameterArray.get(rowIndex);
            switch (columnIndex)
            {
                case 0:
                    p.selectAsUsed((boolean) aValue);
                    break;
                case 2: // only the argument can be changed not the letter
                    p.setCustomValue((String) aValue);
                    break;
            }
        }
        fireTableStructureChanged();
        fireTableUpdatedEvent();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if (parameterArray.isEmpty())
        {
            return Object.class;
        }
        Object obj = getValueAt(0, columnIndex);
        if (obj == null)
        {
            return Object.class;
        }

        return obj.getClass();
    }

    /**
     * Helper enumeration to customize the view onto the table.
     */
    @XmlType
    @XmlEnum(value = String.class)
    public enum ModelMode implements Serializable
    {

        /**
         * Edit the parameter-list.
         */
        EDIT,
        /**
         * Evaluate the parameter-list as positional parameters.
         */
        POSITIONAL_EVALUATED,
        /**
         * Evaluate the parameter-list as letter parameters.
         */
        PARAMLETTER_EVALUATED;
        private static final long serialVersionUID = -8940196742313991701L;

    }
}
