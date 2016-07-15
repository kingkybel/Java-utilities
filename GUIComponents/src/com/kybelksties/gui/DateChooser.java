
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * Custom component to choose a date.
 *
 * @author kybelksd
 */
public class DateChooser extends javax.swing.JPanel
{

    private DateChooserModel dateChooserModel;

    /**
     * Get the value of configuredDate
     *
     * @return the value of configuredDate
     */
    public Date getDate()
    {
        return dateChooserModel.getDate();
    }

    /**
     * Set the value of configuredDate.
     *
     * @param newDate new value of configuredDate
     */
    public void setDate(Date newDate)
    {
        if (newDate != null &&
            !newDate.equals(dateChooserModel.getDate()))
        {
            propertyChangeSupport.firePropertyChange(
                    "configuredDate",
                    dateChooserModel.getDate(),
                    newDate);

        }

        dateChooserModel.setDate(newDate);
    }

    private transient final PropertyChangeSupport propertyChangeSupport =
                                                  new PropertyChangeSupport(
                                                          this);

    /**
     * Set a new locale for this chooser.
     *
     * @param locale the new locale
     */
    public void setDateLocale(Locale locale)
    {
        dateChooserModel.setLocale(locale);
        updateComponents();
    }

    /**
     * Retrieve the locale for this chooser.
     *
     * @return the set locale
     */
    public Locale getDateLocale()
    {
        return dateChooserModel.getLocale();
    }

    private boolean showTable = true;

    /**
     * Get the value of showTable
     *
     * @return the value of showTable
     */
    public boolean isShowTable()
    {
        return showTable;
    }

    /**
     * Set the value of showTable
     *
     * @param showTable new value of showTable
     */
    public void setShowTable(boolean showTable)
    {
        this.showTable = showTable;
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if (propertyChangeSupport != null)
        {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener the listener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private boolean showDateLabel = true;

    /**
     * Get the value of showSample.
     *
     * @return the value of showSample
     */
    public boolean getShowDateLabel()
    {
        return showDateLabel;
    }

    /**
     * Set the value of showSample.
     *
     * @param showSample new value of showSample
     */
    public void setShowDateLabel(boolean showSample)
    {
        this.showDateLabel = showSample;
        updateComponents();
    }

    private boolean showLocaleDropDown = true;

    /**
     * Get the value of showLocaleDropDown
     *
     * @return the value of showLocaleDropDown
     */
    public boolean isShowLocaleDropDown()
    {
        return showLocaleDropDown;
    }

    /**
     * Set the value of showLocaleDropDown
     *
     * @param showLocaleDropDown new value of showLocaleDropDown
     */
    public void setShowLocaleDropDown(boolean showLocaleDropDown)
    {
        this.showLocaleDropDown = showLocaleDropDown;
    }

    JCheckBox getOkButton()
    {
        return okButton;
    }

    JCheckBox getCancelButton()
    {
        return cancelButton;
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
            DateChooserModel model = (DateChooserModel) table.getModel();
            Component c = super.getTableCellRendererComponent(table,
                                                              value,
                                                              isSelected,
                                                              hasFocus,
                                                              row,
                                                              column);
            if ((row == 0 && (int) value > 8) ||
                (row == model.getRowCount() - 1 && (int) value < 10))
            {
                c.setBackground(Color.LIGHT_GRAY);
                c.setForeground(Color.BLACK);
            }
            else if ((int) value == model.getDayOfMonth())
            {
                c.setBackground(Color.CYAN);
                c.setForeground(Color.RED);
            }
            else
            {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLUE);
            }
            return c;
        }
    }

    /**
     * Creates new form DateChooser.
     *
     * @param locale local names and formats etc
     * @param date   a date for which to display a calendar month
     */
    public DateChooser(Locale locale, Date date)
    {
        initComponents();
        setColumnWidths();
        dateChooserModel = new DateChooserModel(locale, date);
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
        yearSpinner.setModel(dateChooserModel);
        monthComboBox.setModel(dateChooserModel);
        monthDayTable.setModel(dateChooserModel);
        monthDayTable.setDefaultRenderer(Object.class,
                                         new MyTableCellRenderer());
        monthComboBox.setModel(new DefaultComboBoxModel(
                dateChooserModel.getMonthsLong().toArray()));

        previewText.setSize(previewText.getHeight(), 70);

        updateComponents();
    }

    /**
     * Default construct.
     */
    public DateChooser()
    {
        this(Locale.getDefault(), null);
    }

    final void setColumnWidths()
    {
        if (monthDayTable.isVisible())
        {
            TableColumnModel colmod = monthDayTable.getColumnModel();
            for (int col = 0; col < colmod.getColumnCount(); col++)
            {
                colmod.getColumn(col).setPreferredWidth(25);
                colmod.getColumn(col).setMinWidth(25);
            }
        }
    }

    /**
     * Update the GUI.
     */
    public final void updateComponents()
    {
        previewText.setVisible(showDateLabel);
        localeComboBox.setVisible(showLocaleDropDown);
        monthDayScrollPane.setVisible(showTable);
        customizationPanel.setVisible(showTable);

        if (dateChooserModel.getLocale() == null)
        {
            dateChooserModel.setLocale(Locale.getDefault());
        }
        if (dateChooserModel.getDate() == null)
        {
            dateChooserModel.setDate(Calendar.getInstance().getTime());
        }
        monthComboBox.setModel(
                new DefaultComboBoxModel(
                        dateChooserModel.getMonthsLong().toArray()));
        monthDayTable.setModel(dateChooserModel);

        for (int i = 0; i < monthDayTable.getColumnCount(); i++)
        {
            TableColumn column = monthDayTable.getTableHeader().
                        getColumnModel().getColumn(i);

            column.setHeaderValue(dateChooserModel.getShortWeekdayName(i));
        }

        monthComboBox.setSelectedItem(dateChooserModel.getMonth());
        yearSpinner.setValue(dateChooserModel.getYear());

        daySpinner.setModel(
                new SpinnerNumberModel(dateChooserModel.getDayOfMonth(),
                                       1,
                                       dateChooserModel.getMaxDayOfMonth(),
                                       1));

        previewText.setText(dateChooserModel.getDateAsString());

        validate();
        repaint();
    }

    /**
     * Show a dialog with this component.
     *
     * @param window parent
     * @param date   an initial date
     * @param locale the locale properties
     * @return the chosen date
     */
    static public Date showDialog(Window window, final Date date, Locale locale)
    {
        final DateChooser chsr = new DateChooser(locale, date);

        final JDialog dlg = new JDialog(window,
                                        Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setTitle("Date Chooser");
        dlg.getContentPane().setLayout(new AbsoluteLayout());
        Dimension chsrDim = chsr.getPreferredSize();
        dlg.getContentPane().setSize(chsrDim);
        AbsoluteConstraints constraints =
                            new AbsoluteConstraints(2,
                                                    2,
                                                    chsrDim.width,
                                                    chsrDim.height);
        dlg.add(chsr, constraints);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chsr.updateComponents();
                dlg.setVisible(false);
            }
        });

        // where to put the OK button
        Dimension okDim = okButton.getPreferredSize();
        constraints = new AbsoluteConstraints(chsrDim.width - okDim.width * 2,
                                              chsrDim.height + 4,
                                              okDim.width,
                                              okDim.height);
        dlg.add(okButton, constraints);

        Dimension dialogDim = new Dimension(chsrDim.width + 4,
                                            chsrDim.height + okDim.height + 40);
        dlg.setMinimumSize(dialogDim);
        dlg.setMaximumSize(dialogDim);
        dlg.setSize(dialogDim);
        Point p1 = window.getLocation();
        Dimension d1 = window.getSize();
        Dimension d2 = dlg.getSize();

        int x = p1.x + (d1.width - d2.width) / 2;
        int y = p1.y + (d1.height - d2.height) / 2;

        if (x < 0)
        {
            x = 0;
        }
        if (y < 0)
        {
            y = 0;
        }

        dlg.setLocation(x, y);

        dlg.setVisible(true);

        return chsr.getDate();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        monthDayScrollPane = new javax.swing.JScrollPane();
        monthDayTable = new javax.swing.JTable();
        customizationPanel = new javax.swing.JPanel();
        yearSpinner = new javax.swing.JSpinner();
        daySpinner = new javax.swing.JSpinner();
        monthComboBox = new javax.swing.JComboBox();
        previewText = new javax.swing.JTextField();
        localeComboBox = new com.kybelksties.gui.LocaleComboBox();
        okButton = new javax.swing.JCheckBox();
        cancelButton = new javax.swing.JCheckBox();

        setName(""); // NOI18N
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        monthDayScrollPane.setMaximumSize(new java.awt.Dimension(452, 220));
        monthDayScrollPane.setMinimumSize(new java.awt.Dimension(452, 220));
        monthDayScrollPane.setPreferredSize(new java.awt.Dimension(452, 220));

        monthDayTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        monthDayTable.setAutoscrolls(false);
        monthDayTable.setMaximumSize(new java.awt.Dimension(450, 240));
        monthDayTable.setMinimumSize(new java.awt.Dimension(450, 240));
        monthDayTable.setPreferredSize(new java.awt.Dimension(450, 240));
        monthDayTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        monthDayTable.setShowHorizontalLines(false);
        monthDayTable.setShowVerticalLines(false);
        monthDayTable.getTableHeader().setResizingAllowed(false);
        monthDayTable.getTableHeader().setReorderingAllowed(false);
        monthDayTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                monthDayTableMouseClicked(evt);
            }
        });
        monthDayScrollPane.setViewportView(monthDayTable);

        add(monthDayScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 300, 140));
        add(customizationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 269, -1, -1));

        yearSpinner.setModel(new javax.swing.SpinnerNumberModel(2016, 0, 2300, 1));
        yearSpinner.setMaximumSize(new java.awt.Dimension(75, 20));
        yearSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                yearSpinnerStateChanged(evt);
            }
        });
        add(yearSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 70, -1));

        daySpinner.setMaximumSize(new java.awt.Dimension(55, 20));
        daySpinner.setMinimumSize(new java.awt.Dimension(55, 20));
        daySpinner.setPreferredSize(new java.awt.Dimension(55, 20));
        daySpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                daySpinnerStateChanged(evt);
            }
        });
        add(daySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, -1));

        monthComboBox.setMaximumSize(new java.awt.Dimension(120, 20));
        monthComboBox.setMinimumSize(new java.awt.Dimension(120, 20));
        monthComboBox.setPreferredSize(new java.awt.Dimension(120, 20));
        monthComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                monthComboBoxActionPerformed(evt);
            }
        });
        add(monthComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 130, -1));

        previewText.setEditable(false);
        previewText.setText(org.openide.util.NbBundle.getMessage(DateChooser.class, "DateChooser.text")); // NOI18N
        previewText.setMaximumSize(null);
        previewText.setMinimumSize(null);
        previewText.setName(""); // NOI18N
        previewText.setPreferredSize(new java.awt.Dimension(110, 24));
        add(previewText, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 130, -1));

        localeComboBox.setMinimumSize(new java.awt.Dimension(0, 0));
        localeComboBox.setPreferredSize(new java.awt.Dimension(160, 22));
        localeComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                localeComboBoxActionPerformed(evt);
            }
        });
        add(localeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 160, -1));

        okButton.setText(org.openide.util.NbBundle.getMessage(DateChooser.class, "DateChooser.okButton.text")); // NOI18N
        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/okTick.png"))); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }
        });
        add(okButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, -1, -1));

        cancelButton.setText(org.openide.util.NbBundle.getMessage(DateChooser.class, "DateChooser.cancelButton.text")); // NOI18N
        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancelTick.png"))); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });
        add(cancelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void monthComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_monthComboBoxActionPerformed
    {//GEN-HEADEREND:event_monthComboBoxActionPerformed
        dateChooserModel.setMonthByIndex(monthComboBox.getSelectedIndex());
        updateComponents();
    }//GEN-LAST:event_monthComboBoxActionPerformed

    private void monthDayTableMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_monthDayTableMouseClicked
    {//GEN-HEADEREND:event_monthDayTableMouseClicked
        int col = monthDayTable.getSelectedColumn();
        int row = monthDayTable.getSelectedRow();

        int newDay = (int) monthDayTable.getValueAt(row, col);
        if (row == 0 && newDay > 7)
        {
            // set prev month
            dateChooserModel.setPreviousMonth();
        }
        else if (row == monthDayTable.getRowCount() - 1 && newDay < 28)
        {
            // set next month
            dateChooserModel.setNextMonth();
        }

        dateChooserModel.setDayOfMonth(newDay);
        updateComponents();
    }//GEN-LAST:event_monthDayTableMouseClicked

    private void yearSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_yearSpinnerStateChanged
    {//GEN-HEADEREND:event_yearSpinnerStateChanged
        updateComponents();
    }//GEN-LAST:event_yearSpinnerStateChanged

    private void localeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_localeComboBoxActionPerformed
    {//GEN-HEADEREND:event_localeComboBoxActionPerformed
        dateChooserModel.setLocale((Locale) localeComboBox.getSelectedItem());
        updateComponents();

    }//GEN-LAST:event_localeComboBoxActionPerformed

    private void daySpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_daySpinnerStateChanged
    {//GEN-HEADEREND:event_daySpinnerStateChanged
        dateChooserModel.setDayOfMonth((int) daySpinner.getValue());
        updateComponents();
    }//GEN-LAST:event_daySpinnerStateChanged

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
    {//GEN-HEADEREND:event_okButtonActionPerformed
        // setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        // setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cancelButton;
    private javax.swing.JPanel customizationPanel;
    private javax.swing.JSpinner daySpinner;
    private com.kybelksties.gui.LocaleComboBox localeComboBox;
    private javax.swing.JComboBox monthComboBox;
    private javax.swing.JScrollPane monthDayScrollPane;
    private javax.swing.JTable monthDayTable;
    private javax.swing.JCheckBox okButton;
    private javax.swing.JTextField previewText;
    private javax.swing.JSpinner yearSpinner;
    // End of variables declaration//GEN-END:variables
    private static final String CLASS_NAME = DateChooser.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
}
