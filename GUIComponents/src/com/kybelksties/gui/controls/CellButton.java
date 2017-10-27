/*
 * @author  Dieter J Kybelksties
 * @date Apr 6, 2017
 *
 */
package com.kybelksties.gui.controls;

import java.awt.Component;
import java.util.EventObject;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Dieter J Kybelksties
 */
public abstract class CellButton
        extends JButton
        implements TableCellEditor
{

    public CellButton()
    {
    }

    public CellButton(Icon icon)
    {
        super(icon);
    }

    public CellButton(String text)
    {
        super(text);
    }

    public CellButton(Action a)
    {
        super(a);
    }

    public CellButton(String text, Icon icon)
    {
        super(text, icon);
    }

    abstract public Object getValue();

    abstract public void setValue(Object o);

    abstract void commitEdit();

    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column)
    {
        return this;
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l)
    {

    }

    @Override
    public void addCellEditorListener(CellEditorListener l)
    {

    }

    @Override
    public void cancelCellEditing()
    {

    }

    @Override
    public boolean stopCellEditing()
    {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent)
    {
        return true;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent)
    {
        return true;
    }

    @Override
    public Object getCellEditorValue()
    {
        return getValue();
    }

}
