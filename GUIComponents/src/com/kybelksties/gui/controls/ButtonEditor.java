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
package com.kybelksties.gui.controls;

import java.awt.Component;
import java.util.EventObject;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * A table cell editor for numeric values. Uses the cellButton control to change
 * the cell value.
 *
 * @author kybelksd
 */
public class ButtonEditor extends DefaultCellEditor
{

    private static final Class CLAZZ = ButtonEditor.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    CellButton cellButton;
    JTextField textField;
    boolean valueSet;
    AbstractCellEditor editor;

    /**
     * Initialises the cellButton.
     *
     * @param cellButton
     */
    public ButtonEditor(CellButton cellButton)
    {
        super(new JTextField());
        this.cellButton = cellButton;
    }

    // Prepares the cellButton component and returns it.
    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column)
    {
        if (!valueSet)
        {
            cellButton.setValue(value);
        }
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                getComponent().requestFocus();
            }
        });
        return cellButton;
    }

    @Override
    public boolean isCellEditable(EventObject eo)
    {
        return super.isCellEditable(eo);
//        if (eo instanceof KeyEvent)
//        {
//            KeyEvent ke = (KeyEvent) eo;
//
//            getComponent().setText(String.valueOf(ke.getKeyChar()));
//            //textField.select(1,1);
//            //textField.setCaretPosition(1);
//            //textField.moveCaretPosition(1);
//            valueSet = true;
//        }
//        else
//        {
//            valueSet = false;
//        }
//        return true;
    }

    // Returns the spinners current value.
    @Override
    public Object getCellEditorValue()
    {
        return cellButton.getValue();
    }

    @Override
    public boolean stopCellEditing()
    {
        cellButton.commitEdit();
        return super.stopCellEditing();
    }
}
