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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import org.openide.util.NbBundle;

/**
 * A table cell editor for numeric values. Uses the spinner control to change
 * the cell value.
 *
 * @author kybelksd
 */
public class SpinnerEditor extends DefaultCellEditor
{

    private static final Class CLAZZ = SpinnerEditor.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    JSpinner spinner;
    JSpinner.DefaultEditor editor;
    JTextField textField;
    boolean valueSet;

    /**
     * Initializes the spinner.
     */
    public SpinnerEditor()
    {
        super(new JTextField());
        spinner = new JSpinner();
        editor = ((JSpinner.DefaultEditor) spinner.getEditor());
        textField = editor.getTextField();
        textField.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent fe)
            {
                //textField.setSelectionStart(0);
                //textField.setSelectionEnd(1);
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (valueSet)
                        {
                            textField.setCaretPosition(1);
                        }
                    }
                });
            }

            @Override
            public void focusLost(FocusEvent fe)
            {
            }
        });
        textField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                stopCellEditing();
            }
        });
    }

    /**
     * Construct the Spinner editor.
     *
     * @param <T> a numeric type
     * @param t   initial value of type T
     */
    public <T extends Number> SpinnerEditor(T t)
    {
        this();
        spinner.setValue(t);

    }

    /**
     * Construct the Spinner editor.
     *
     * @param value initial value
     * @param min   minimal value
     * @param max   maximal value
     * @param step  increment step
     */
    public SpinnerEditor(int value, int min, int max, int step)
    {
        this();
        SpinnerModel modeltau = new SpinnerNumberModel(value, min, max, step);
        spinner = new JSpinner(modeltau);
        spinner.setValue(value);
    }

    /**
     * Construct the Spinner editor.
     *
     * @param value initial value
     * @param min   minimal value
     * @param max   maximal value
     * @param step  increment step
     */
    public SpinnerEditor(double value, double min, double max, double step)
    {
        this();
        SpinnerModel modeltau = new SpinnerNumberModel(value, min, max, step);
        spinner = new JSpinner(modeltau);
        spinner.setValue(value);
    }

    // Prepares the spinner component and returns it.
    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column)
    {
        if (!valueSet)
        {
            spinner.setValue(value);
        }
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                textField.requestFocus();
            }
        });
        return spinner;
    }

    @Override
    public boolean isCellEditable(EventObject eo)
    {
        if (eo instanceof KeyEvent)
        {
            KeyEvent ke = (KeyEvent) eo;

            textField.setText(String.valueOf(ke.getKeyChar()));
            //textField.select(1,1);
            //textField.setCaretPosition(1);
            //textField.moveCaretPosition(1);
            valueSet = true;
        }
        else
        {
            valueSet = false;
        }
        return true;
    }

    // Returns the spinners current value.
    @Override
    public Object getCellEditorValue()
    {
        return spinner.getValue();
    }

    @Override
    public boolean stopCellEditing()
    {
        try
        {
            editor.commitEdit();
            spinner.commitEdit();
        }
        catch (java.text.ParseException e)
        {
            JOptionPane.showMessageDialog(null,
                                          NbBundle.getMessage(
                                                  CLAZZ,
                                                  "SpinnerEditor.invalidValue"));
        }
        return super.stopCellEditing();
    }
}
