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
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Implementation of the TableCellEditor interface that allows each
 * row/column/cell to be assigned an individual cell-editor.
 * <p>
 * <b>Usage:</b> an IndividualCellEditor maintains a name/editor-component map.
 * Users initially create an appropriate set of editors as per their
 * requirements. Every IndividualCellEditor has a default (text) editor defined,
 * that can be selected by the map-key "default". This editor will be used if no
 * other editors are defined or no other editor is applicable to a particular
 * table-cell.
 * <p>
 * insert[<b>type</b>]Editor() - functions are used to add to cell-editors to
 * the map with <b>type</b> one of
 * <ul>
 * <li><b>Text</b>: text editor</li>
 * <li><b>Checkbox</b>: check-box for boolean values</li>
 * <li><b>Spinner</b>: spinner component for integer/double values</li>
 * <li><b>Combo</b>: combo box for a list of specific values</li>
 * </ul>
 *
 * Cell editors can be applied to all table cells, individual rows or columns,
 * and individual cells. The decision whether a cell editor should be applied to
 * a cell can be made dependent on the cell's value or another cell's value.
 *
 * <p>
 * <b>Example:</b>
 * <pre>
 * {@code
 * ice = new IndividualCellEditor(this, new DefaultTableCellRenderer());
 * Object[] types = new Object[] {  "DropdownValue1",
 *                                  "DropdownValue2",
 *                                  "DropdownValue3"};
 * ice.insertComboEditor("TypeCombo", types);
 * ice.insertSpinnerEditor("IntValueSpinner", 1, -10000, 10000, 1);
 * ice.insertSpinnerEditor("DoubleValueSpinner", 1.0, -10000.0, 10000.0, 1.0);
 * ice.insertCheckboxEditor("BoolValueCheck");
 *
 * ice.setColumnEditor(0, "BoolValueCheck");
 * // if the selected Type (in column 2) is "DropdownValue1"
 * ice.setColumnValueDependentColumnEditor(3,
 *                                         "DoubleValueSpinner",
 *                                         2,
 *                                         "DropdownValue1");
 * }
 * </pre>
 */
public class IndividualCellEditor implements TableCellEditor
{

    private static final Class CLAZZ = IndividualCellEditor.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * A set of installed TableCellEditor's that can be applied to
     * cell(-range)s.
     */
    private HashMap<String, TableCellEditor> installedEditors = new HashMap<>();
    private HashMap<String, Component> installedComponents = new HashMap<>();
    private HashMap<Point, Component> xyComponents = new HashMap<>();

    private TableCellEditor editor;

    JTable table;
    TreeSet<EditorCond> appliedEditors = new TreeSet<>();

    /**
     * Constructs a IndividualCellEditor. Create default editor.
     *
     * @param table the underlying table
     * @see TableCellEditor
     * @see TableCellRenderer
     * @see DefaultCellEditor
     */
    public IndividualCellEditor(JTable table)
    {
        setRendererOnTable(table, new DefaultTableCellRenderer());
        table.setRowHeight(20);
        insertTextEditor("default");
        editor = installedEditors.get("default");
    }

    /**
     * Constructs a IndividualCellEditor. Create default editor.
     *
     * @param table    the underlying table
     * @param renderer any cell-renderer
     * @see TableCellEditor
     * @see TableCellRenderer
     * @see DefaultCellEditor
     */
    public IndividualCellEditor(JTable table, TableCellRenderer renderer)
    {
        this(table);
        setRendererOnTable(table, renderer);
    }

    private int findColumn(String header)
    {
        if (table == null)
        {
            return -1;
        }

        TableModel mod = table.getModel();
        if (mod == null)
        {
            return -1;
        }
        for (int col = 0; col < mod.getColumnCount(); col++)
        {
            if (mod.getColumnName(col).equals(header))
            {
                return col;
            }
        }
        return -1;
    }

    /**
     * Set a renderer on the table.
     *
     * @param ref_table new table
     * @param renderer  new renderer
     */
    public final void setRendererOnTable(JTable ref_table,
                                         TableCellRenderer renderer)
    {
        table = ref_table;
        for (int i = 0; i < table.getColumnCount(); i++)
        {
            table.getColumn(table.getColumnName(i)).setCellEditor(this);
            table.getColumn(table.getColumnName(i)).setCellRenderer(renderer);
        }
    }

    /**
     * Add a text-editor identified by a (unique) name.
     *
     * @param name unique name
     */
    public final void insertTextEditor(String name)
    {
        JTextField textField = new JTextField();
        insertComponent(name, textField);
        installedEditors.put(name, new DefaultCellEditor(textField));
    }

    /**
     * Add a check-box-editor identified by a (unique) name.
     *
     * @param name unique name
     */
    public void insertCheckboxEditor(String name)
    {
        JCheckBox checkBox = new JCheckBox();
        insertComponent(name, checkBox);
        installedEditors.put(name, new DefaultCellEditor(checkBox));
    }

    /**
     * Add a button-editor identified by a (unique) name. Need to create the
     * CellButton - derived object and pass as parameter.
     *
     * @param name       unique name
     * @param cellButton the button component
     */
    public void insertButtonEditor(String name, CellButton cellButton)
    {
        insertComponent(name, cellButton);
        installedEditors.put(name, cellButton);
    }

    private void insertComponent(String name, Component component)
    {
        installedComponents.put(name, component);
    }

    /**
     * Add an integer valued spinner editor identified by a (unique) name.
     *
     * @param name     unique name
     * @param value    initial value
     * @param minValue minimal value
     * @param maxValue maximal value
     * @param step     increment step
     */
    public void insertSpinnerEditor(String name,
                                    Integer value,
                                    Integer minValue,
                                    Integer maxValue,
                                    Integer step)
    {
        SpinnerEditor spinnerEditor = new SpinnerEditor(value,
                                                        minValue,
                                                        maxValue,
                                                        step);
        insertComponent(name, spinnerEditor.getComponent());
        installedEditors.put(name, spinnerEditor);
    }

    /**
     * Add a double valued spinner editor identified by a (unique) name.
     *
     * @param name     unique name
     * @param value    initial value
     * @param minValue minimal value
     * @param maxValue maximal value
     * @param step     increment step
     */
    public void insertSpinnerEditor(String name,
                                    Double value,
                                    Double minValue,
                                    Double maxValue,
                                    Double step)
    {
        SpinnerEditor spinnerEditor = new SpinnerEditor(value,
                                                        minValue,
                                                        maxValue,
                                                        step);
        insertComponent(name, spinnerEditor.getComponent());
        installedEditors.put(name, spinnerEditor);
    }

    /**
     * Add a combo-box-editor identified by a (unique) name.
     *
     * @param name    unique name
     * @param choices combo-choices as an array
     */
    public final void insertComboEditor(String name, Object[] choices)
    {
        List<Object> s = Arrays.asList(choices);
        insertComboEditor(name, s);
    }

    /**
     * Add a text-editor identified by a (unique) name.
     *
     * @param name    unique name
     * @param choices combo-choices as a list
     */
    public final void insertComboEditor(String name, List choices)
    {
        JComboBox newCombo = new JComboBox();
        for (Object o : choices)
        {
            newCombo.addItem(o);
        }

        newCombo.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                final JComponent c = (JComponent) e.getSource();
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        c.requestFocus();
                        System.out.println(c);
                        if (c instanceof JComboBox)
                        {
                            System.out.println("a");
                        }
                    }
                });
            }
        });
        insertComponent(name, newCombo);
        installedEditors.put(name, new DefaultCellEditor(newCombo));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column)
    {
        Component reval = editor.getTableCellEditorComponent(table,
                                                             value,
                                                             isSelected,
                                                             row,
                                                             column);
        return reval;
    }

    @Override
    public Object getCellEditorValue()
    {
        return editor.getCellEditorValue();
    }

    @Override
    public boolean stopCellEditing()
    {
        return editor.stopCellEditing();
    }

    @Override
    public void cancelCellEditing()
    {
        editor.cancelCellEditing();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent)
    {
        if (anEvent instanceof MouseEvent)
        {
            selectEditor((MouseEvent) anEvent);
        }
        return editor.isCellEditable(anEvent);
    }

    @Override
    public void addCellEditorListener(CellEditorListener l)
    {
        editor.addCellEditorListener(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l)
    {
        if (l != null)
        {
            editor.removeCellEditorListener(l);
        }
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent)
    {
        selectEditor((MouseEvent) anEvent);
        return editor.shouldSelectCell(anEvent);
    }

    /**
     * Set the named editor for a row.
     *
     * @param row        the row the editor will be applied to
     * @param editorName the name of the configured editor to use
     */
    public void setRowEditor(int row, String editorName)
    {
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.NONE,
                                       -1,
                                       EditorCond.AppliesTo.ROW,
                                       row,
                                       null);
        addAppliedEditor(ec, editorName);
    }

    /**
     * Set the named editor for a row dependent on a value in a column value in
     * that row.
     *
     * @param editorName        the name of the configured editor to use
     * @param dependentOnColumn the column on which we decide which editor to
     *                          use
     * @param value             the deciding cell value
     */
    public void setColumnValueDependentRowEditor(String editorName,
                                                 int dependentOnColumn,
                                                 Object value)
    {
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.COLUMN_VALUE,
                                       dependentOnColumn,
                                       EditorCond.AppliesTo.ROW,
                                       -1,
                                       value);
        addAppliedEditor(ec, editorName);
    }

    private boolean addAppliedEditor(EditorCond ec, String name)
    {
        boolean reval = appliedEditors.add(ec);

        for (int col = 0; col < table.getModel().getColumnCount(); col++)
        {
            for (int row = 0; row < table.getModel().getRowCount(); row++)
            {
                if (ec.isApplicable(col, row, table))
                {
                    xyComponents.put(new Point(col, row),
                                     installedComponents.get(name));
                }
            }
        }
        return reval;
    }

    /**
     * Set the named editor for a column.
     *
     * @param column     the column the editor will be applied to
     * @param editorName the name of the configured editor to use
     */
    public void setDefaultEditor(int column, String editorName)
    {
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.NONE,
                                       -1,
                                       EditorCond.AppliesTo.ALL,
                                       column,
                                       null);
        addAppliedEditor(ec, editorName);
    }

    /**
     * Set the named editor for a column.
     *
     * @param column     the column the editor will be applied to
     * @param editorName the name of the configured editor to use
     */
    public void setColumnEditor(int column, String editorName)
    {
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.NONE,
                                       -1,
                                       EditorCond.AppliesTo.COLUMN,
                                       column,
                                       null);
        addAppliedEditor(ec, editorName);
    }

    /**
     * Set the named editor for a column.
     *
     * @param columnName the column the editor will be applied to
     * @param editorName the name of the configured editor to use
     */
    public void setColumnEditor(String columnName, String editorName)
    {
        int column = findColumn(columnName);
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.NONE,
                                       -1,
                                       EditorCond.AppliesTo.COLUMN,
                                       column,
                                       null);
        addAppliedEditor(ec, editorName);
    }

    /**
     * Set the named editor for a column dependent on a value in a row value in
     * that column.
     *
     * @param column         the column to which the editor will be applied
     * @param editorName     the name of the configured editor to use
     * @param dependentOnRow the row on which we decide which editor to use
     * @param value          the deciding cell value
     */
    public void setColumnEditor(int column,
                                String editorName,
                                int dependentOnRow,
                                Object value)
    {
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.ROW_VALUE,
                                       dependentOnRow,
                                       EditorCond.AppliesTo.ROW,
                                       column,
                                       value);
        addAppliedEditor(ec, editorName);
    }

    /**
     * Set the named editor for a column dependent on a value in a row value in
     * that column.
     *
     * @param columnName     the column to which the editor will be applied
     * @param editorName     the name of the configured editor to use
     * @param dependentOnRow the row on which we decide which editor to use
     * @param value          the deciding cell value
     */
    public void setColumnEditor(String columnName,
                                String editorName,
                                int dependentOnRow,
                                Object value)
    {
        int column = findColumn(columnName);
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.ROW_VALUE,
                                       dependentOnRow,
                                       EditorCond.AppliesTo.ROW,
                                       column,
                                       value);
        addAppliedEditor(ec, editorName);
    }

    /**
     *
     * @param editorName
     * @param dependentOnColumn
     * @param value
     */
    public void setRowValueDependentColumnEditor(String editorName,
                                                 int dependentOnColumn,
                                                 Object value)
    {
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.ROW_VALUE,
                                       dependentOnColumn,
                                       EditorCond.AppliesTo.ROW,
                                       -1,
                                       value);
        addAppliedEditor(ec, editorName);
    }

    /**
     *
     * @param editorName
     * @param dependentOnColumnName
     * @param value
     */
    public void setRowValueDependentColumnEditor(String editorName,
                                                 String dependentOnColumnName,
                                                 Object value)
    {
        int dependentOnColumn = findColumn(dependentOnColumnName);
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.ROW_VALUE,
                                       dependentOnColumn,
                                       EditorCond.AppliesTo.ROW,
                                       -1,
                                       value);
        addAppliedEditor(ec, editorName);
    }

    /**
     *
     * @param column
     * @param editorName
     * @param dependentOnColumn
     * @param value
     */
    public void setColumnValueDependentColumnEditor(int column,
                                                    String editorName,
                                                    int dependentOnColumn,
                                                    Object value)
    {
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.COLUMN_VALUE,
                                       dependentOnColumn,
                                       EditorCond.AppliesTo.COLUMN,
                                       column,
                                       value);
        addAppliedEditor(ec, editorName);
    }

    /**
     *
     * @param columnName
     * @param editorName
     * @param dependentOnColumnName
     * @param value
     */
    public void setColumnValueDependentColumnEditor(String columnName,
                                                    String editorName,
                                                    String dependentOnColumnName,
                                                    Object value)
    {
        int column = findColumn(columnName);
        int dependentOnColumn = findColumn(dependentOnColumnName);
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.COLUMN_VALUE,
                                       dependentOnColumn,
                                       EditorCond.AppliesTo.COLUMN,
                                       column,
                                       value);
        addAppliedEditor(ec, editorName);
    }

    /**
     *
     * @param row
     * @param editorName
     * @param dependentOnRow
     * @param value
     */
    public void setRowValueDependentRowEditor(int row,
                                              String editorName,
                                              int dependentOnRow,
                                              Object value)
    {
        EditorCond ec = new EditorCond(editorName,
                                       EditorCond.DependsOn.ROW_VALUE,
                                       dependentOnRow,
                                       EditorCond.AppliesTo.ROW,
                                       row,
                                       value);
        addAppliedEditor(ec, editorName);
    }

    /**
     *
     * @param column
     * @param row
     * @param editorName
     */
    public void setIndiviualCellEditor(int column,
                                       int row,
                                       String editorName)
    {
        EditorCond ec = new EditorCond(editorName,
                                       -1,
                                       -1,
                                       row,
                                       column,
                                       null);
        addAppliedEditor(ec, editorName);
    }

    /**
     *
     * @param columnName
     * @param row
     * @param editorName
     */
    public void setIndiviualCellEditor(String columnName,
                                       int row,
                                       String editorName)
    {
        int column = findColumn(columnName);
        EditorCond ec = new EditorCond(editorName,
                                       -1,
                                       -1,
                                       row,
                                       column,
                                       null);
        addAppliedEditor(ec, editorName);
    }

    /**
     *
     * @param column
     * @param row
     * @param dependentOnCol
     * @param dependentOnRow
     * @param editorName
     */
    public void setCellDependentIndiviualCellEditor(int column,
                                                    int row,
                                                    int dependentOnCol,
                                                    int dependentOnRow,
                                                    String editorName)
    {
        EditorCond ec = new EditorCond(editorName,
                                       dependentOnCol,
                                       dependentOnRow,
                                       row,
                                       column,
                                       null);
        addAppliedEditor(ec, editorName);
    }

    /**
     *
     * @param columnName
     * @param row
     * @param dependentOnColumnName
     * @param dependentOnRow
     * @param editorName
     */
    public void setCellDependentIndiviualCellEditor(String columnName,
                                                    int row,
                                                    String dependentOnColumnName,
                                                    int dependentOnRow,
                                                    String editorName)
    {
        int column = findColumn(columnName);
        int dependentOnColumn = findColumn(
            dependentOnColumnName);
        EditorCond ec = new EditorCond(editorName,
                                       dependentOnColumn,
                                       dependentOnRow,
                                       row,
                                       column,
                                       null);
        addAppliedEditor(ec, editorName);
    }

    /**
     * When a row condition evaluates to true and a column condition evaluates
     * to true then prefer row condition.
     */
    public void preferRow()
    {
        for (EditorCond cond : appliedEditors)
        {
            cond.preferRow = true;
        }
    }

    /**
     * When a row condition evaluates to true and a column condition evaluates
     * to true then prefer column condition.
     */
    public void preferColumn()
    {
        for (EditorCond cond : appliedEditors)
        {
            cond.preferRow = false;
        }
    }

    /**
     *
     * @param e
     */
    protected void selectEditor(MouseEvent e)
    {
        int row;
        int col;
        if (e == null)
        {
            row = table.getSelectionModel().getAnchorSelectionIndex();
            col = table.getSelectionModel().getAnchorSelectionIndex();
        }
        else
        {
            row = table.rowAtPoint(e.getPoint());
            col = table.columnAtPoint(e.getPoint());
        }

        for (EditorCond ec : appliedEditors)
        {
            if (ec.isApplicable(col, row, table))
            {
                // now check, whether this value matches the condition and
                // return the editor
                String name = ec.editor();

                editor = installedEditors.get(name);

                table.setCellEditor(editor);
                return;
            }

        }

        editor = installedEditors.get("default");

        table.setCellEditor(editor);
    }

    /**
     * Retrieve the editor component at cell (row,column)
     *
     * @param row    the row in the table
     * @param column the column in the table
     * @return the editor for the cell
     */
    public Component getEditorComponent(int row, int column)
    {
        return xyComponents.get(new Point(row, column));
    }

    static class EditorCond implements Comparable
    {

        String editor = "default";
        DependsOn dependsOnType = DependsOn.NONE;
        AppliesTo appliesToType = AppliesTo.ALL;

        int appliesToCol = -1;
        int appliesToRow = -1;
        boolean preferRow = true;
        int dependOnCol = -1; // column of the cell we depend on
        int dependOnRow = -1; // row of the cell we depend on
        int lowIndex = -1; // -1: start with first
        int highIndex = -1; // -1: end with last
        boolean applyToDependentIndex = false; // use the editor for all but this index
        Object value; // if value at index 'dependentIndex' equals this

        /**
         * Default construct.
         */
        EditorCond()
        {
        }

        /**
         * Construct an editor condition.
         *
         * @param editor name of the editor for which the condition will be
         *               applied
         */
        EditorCond(String editor)
        {
            this();
            this.editor = editor;
        }

        /**
         * Construct an editor condition.
         *
         * @param editor        name of the editor for which the condition will
         *                      be applied
         * @param dependsOnType type of dependency (row-value/column-value/...)
         * @param dependOnIndex (column-/row-) index used for the condition
         * @param appliesToType applicable to all cells/ rows only/...
         * @param applyToIndex  index of row/column this condition is applicable
         *                      to
         * @param value         condition uses this value for the condition
         *                      (null if no dependency on a value)
         * @param bounds        condition might be bounded on this range of
         *                      columns/rows
         */
        EditorCond(String editor,
                   DependsOn dependsOnType,
                   int dependOnIndex,
                   AppliesTo appliesToType,
                   int applyToIndex,
                   Object value,
                   Integer... bounds)
        {
            this(editor);

            this.dependsOnType = dependsOnType;
            if (this.dependsOnType == DependsOn.COLUMN_VALUE)
            {
                dependOnCol = dependOnIndex;
            }
            else if (this.dependsOnType == DependsOn.ROW_VALUE)
            {
                dependOnRow = dependOnIndex;
            }
            else // DependsOn.INDIVIDUAL_CELL is a mistake here
            {
                this.dependsOnType = DependsOn.NONE;
            }

            this.appliesToType = appliesToType;
            if (this.appliesToType == AppliesTo.COLUMN)
            {
                appliesToCol = applyToIndex;
            }
            else if (this.appliesToType == AppliesTo.ROW)
            {
                appliesToRow = applyToIndex;
            }
            else // AppliesTo.ONE_CELL is a mistake here
            {
                this.appliesToType = AppliesTo.ALL;
            }

            this.value = value;

            if (bounds != null)
            {
                if (bounds.length > 0)
                {
                    this.lowIndex = bounds[0];
                }
                if (bounds.length > 1)
                {
                    this.highIndex = bounds[1];
                }
            }
        }

        /**
         * Construct an editor condition.
         *
         * @param editor      name of the editor for which the condition will be
         *                    applied
         * @param dependOnCol column-index which form part of the condition (-1
         *                    if no column is part of the condition)
         * @param dependOnRow row-index which form part of the condition (-1 if
         *                    no row is part of the condition)
         * @param applyToCol  column index to which the condition will be
         *                    applied (-1 for all columns)
         * @param applyToRow  row index to which the condition will be applied
         *                    (-1 for all rows)
         * @param value       condition uses this value for the condition (null
         *                    if no dependency on a value)
         * @param bounds      condition might be bounded on this range of
         *                    columns/rows
         */
        EditorCond(String editor,
                   int dependOnCol,
                   int dependOnRow,
                   int applyToCol,
                   int applyToRow,
                   Object value,
                   Integer... bounds)
        {
            this(editor);

            this.dependsOnType =
            dependOnCol == -1 && dependOnRow == -1 ? DependsOn.NONE :
            dependOnCol == -1 ? DependsOn.ROW_VALUE :
            dependOnRow == -1 ? DependsOn.COLUMN_VALUE :
            DependsOn.INDIVIDUAL_CELL;

            this.dependOnCol = dependOnCol;
            this.dependOnRow = dependOnRow;

            this.appliesToType =
            applyToCol == -1 && applyToRow == -1 ? AppliesTo.ALL :
            applyToCol == -1 ? AppliesTo.COLUMN :
            applyToRow == -1 ? AppliesTo.ROW :
            AppliesTo.ONE_CELL;

            this.value = value;

            if (bounds != null)
            {
                if (bounds.length > 0)
                {
                    this.lowIndex = bounds[0];
                }
                if (bounds.length > 1)
                {
                    this.highIndex = bounds[1];
                }
            }
        }

        /**
         * This test checks whether this cell-editor should be applied on a cell
         * of the given table. It is called while traversing the hierarchy of
         * editors which is according to granularity.
         *
         * @param col   the column of the cell to test
         * @param row   the row of the cell to test
         * @param table the table we examine
         * @return true, if the editor is applicable, false otherwise
         */
        private boolean isApplicable(int col, int row, JTable table)
        {
            // never applicable for cells outside the range of the table
            if (col < 0 ||
                row < 0 ||
                col >= table.getColumnCount() ||
                row >= table.getRowCount())
            {
                return false;
            }

            if (appliesToType.equals(AppliesTo.ALL) ||
                (appliesToType.equals(AppliesTo.COLUMN) && col == appliesToCol) ||
                (appliesToType.equals(AppliesTo.ROW) && row == appliesToRow) ||
                (col == appliesToCol && row == appliesToRow))
            {
                if (dependsOnType.equals(DependsOn.NONE))
                {
                    return true;
                }
                if (dependsOnType.equals(DependsOn.INDIVIDUAL_CELL))
                {
                    return value.equals(table.getValueAt(dependOnRow,
                                                         dependOnCol));
                }
                if (dependsOnType.equals(DependsOn.ROW_VALUE))
                {
                    return value.equals(table.getValueAt(dependOnRow, col));
                }
                if (dependsOnType.equals(DependsOn.COLUMN_VALUE))
                {
                    return value.equals(table.getValueAt(row, dependOnCol));
                }
            }

            return false;
        }

        @Override
        public int compareTo(Object o)
        {
            if (o == null)
            {
                return -1;
            }
            if (o.getClass() != EditorCond.class)
            {
                return -1;
            }
            EditorCond other = (EditorCond) o;

            // individual cell editors are preferred
            if (dependsOnType == DependsOn.INDIVIDUAL_CELL &&
                other.dependsOnType != DependsOn.INDIVIDUAL_CELL)
            {
                return -1;
            }
            if (preferRow)
            {
                if (dependsOnType != other.dependsOnType)
                {
                    return dependsOnType.ordinal() - other.dependsOnType.
                           ordinal();
                }
                else
                {
                    return editor.compareTo(other.editor); // alphabetical
                }
            }
            else // prefer column
            {
                if (dependsOnType != other.dependsOnType)
                {
                    if ((dependsOnType == DependsOn.ROW_VALUE &&
                         other.dependsOnType ==
                         DependsOn.COLUMN_VALUE) ||
                        (dependsOnType == DependsOn.COLUMN_VALUE &&
                         other.dependsOnType == DependsOn.ROW_VALUE) ||
                        (dependsOnType == DependsOn.ROW_VALUE &&
                         other.dependsOnType == DependsOn.COLUMN_VALUE) ||
                        (dependsOnType == DependsOn.COLUMN_VALUE &&
                         other.dependsOnType == DependsOn.ROW_VALUE))
                    {
                        return other.dependsOnType.ordinal() - dependsOnType.
                               ordinal();
                    }
                    return dependsOnType.ordinal() - other.dependsOnType.
                           ordinal();
                }
                else
                {
                    return editor.compareTo(other.editor); // alphabetical
                }
            }
        }

        /**
         * Get the editor for cell at (col,row) under the condition of the
         * dependent value.
         *
         * @return the editor name
         */
        public String editor()
        {
            return editor;
        }

        public static enum DependsOn
        {

            INDIVIDUAL_CELL, ROW_VALUE, COLUMN_VALUE, NONE
        }

        public static enum AppliesTo
        {

            ONE_CELL, ROW, COLUMN, ALL
        }
    }

}
