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
package com.kybelksties.gui;

import com.kybelksties.general.EnvironmentVarTableModel;
import com.kybelksties.general.PodVariant;
import java.awt.Component;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * A JTable extension to display typed/defaulted environment variables.
 *
 * @author kybelksd
 */
public class EnvironmentVarTable extends javax.swing.JTable
{

    private static final Class CLAZZ = EnvironmentVarTable.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    IndividualCellEditor ice;

    /**
     * Construct using a table model.
     *
     * @param etm environment table model
     */
    public EnvironmentVarTable(EnvironmentVarTableModel etm)
    {
        super(etm);
        setIndividualCellEditors(etm);
    }

    /**
     * Construct using a table model and a column model.
     *
     * @param etm environment table model
     * @param cm  column model
     */
    public EnvironmentVarTable(EnvironmentVarTableModel etm, TableColumnModel cm)
    {
        super(etm, cm);
        setIndividualCellEditors(etm);
    }

    /**
     * Construct using a table model, a column model and a list selection model.
     *
     * @param etm environment table model
     * @param cm  column model
     * @param sm  list selection model
     */
    public EnvironmentVarTable(EnvironmentVarTableModel etm,
                               TableColumnModel cm,
                               ListSelectionModel sm)
    {
        super(etm, cm, sm);
        setIndividualCellEditors(etm);
    }

    /**
     * Set individual cell editors specifically needed when entering environment
     * variables. The values will be plain old data types so editors will be:
     * <ul>
     * <li> check boxes for boolean value </li>
     * <li> combo boxes for enumerations/lists </li>
     * <li> spinners for integer and double values </li>
     * <li> and the default string editor for all others </li>
     * </ul>
     *
     * @param etm environment variable table model
     */
    public final void setIndividualCellEditors(EnvironmentVarTableModel etm)
    {
        ice = new IndividualCellEditor(this, new DefaultTableCellRenderer());
        Object[] types = PodVariant.Type.values();
        ice.insertCheckboxEditor("DefineCheck");
        ice.insertComboEditor("TypeCombo", types);
        ice.insertSpinnerEditor("IntValueSpinner", 1, -10000, 10000, 1);
        ice.insertSpinnerEditor("DoubleValueSpinner", 1.0, -10000.0, 10000.0,
                                1.0);
        ice.insertCheckboxEditor("BoolValueCheck");

        // Table columns:
        //    "Def/Undef"(0), "Variable"(1),  "Type"(2), "Value"(3)
        // do define/undefine with a check box
        ice.setColumnEditor(0, "DefineCheck");

        // Variable name is not editable
        // do the Type this a drop-down combo
        ice.setColumnEditor(2, "TypeCombo");

        // select an appropriate editor for the type
        // if the selected Type (in column 2) is a PodVariant.Type.INTEGER
        ice.setColumnValueDependentColumnEditor(3,
                                                "IntValueSpinner",
                                                2,
                                                PodVariant.Type.INTEGER);

        // if the selected Type (in column 2) is a PodVariant.Type.DOUBLE
        ice.setColumnValueDependentColumnEditor(3,
                                                "DoubleValueSpinner",
                                                2,
                                                PodVariant.Type.DOUBLE);

        // if the selected Type (in column 2) is a PodVariant.Type.BOOLEAN
        ice.setColumnValueDependentColumnEditor(3,
                                                "BoolValueCheck",
                                                2,
                                                PodVariant.Type.BOOLEAN);
        setAutoCreateRowSorter(true);
        setModel(etm);
        setCellEditor(ice);
        setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);

        setRowHeight(20);
    }

    static class MyTableCellRenderer extends DefaultTableCellRenderer
    {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column)
        {
            EnvironmentVarTable evt = (EnvironmentVarTable) table;
            IndividualCellEditor ice =
                                 (IndividualCellEditor) evt.getCellEditor();
            Component c = ice.getEditorComponent(row, column);
//            c.setBackground(model.getColor());
//            c.setForeground(model.getContrastColor(row));
            return c;
        }
    }
}
