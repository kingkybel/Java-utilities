
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
import static java.awt.image.ImageObserver.WIDTH;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JTable;
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
//    static private Locale locale = Locale.getDefault();

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
                c.setForeground(Color.YELLOW);
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
        monthComboBox.setModel(new DefaultComboBoxModel(dateChooserModel.
                getMonthsLong().toArray()));

        selectedDateLabel.setSize(selectedDateLabel.getHeight(), 70);

        this.setSize(WIDTH, WIDTH);
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
        selectedDateLabel.setVisible(showDateLabel);
        localeComboBox.setVisible(showLocaleDropDown);
        if (dateChooserModel.getLocale() == null)
        {
            dateChooserModel.setLocale(Locale.getDefault());
        }
        if (dateChooserModel.getDate() == null)
        {
            dateChooserModel.setDate(Calendar.getInstance().getTime());
        }
//        if (!locale.equals(dateChooserModel.getLocale()))
        {
//            dateChooserModel.setLocale(locale);
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
        }
//        dateChooserModel.setDate(date);
        monthComboBox.setSelectedItem(dateChooserModel.getMonth());
        yearSpinner.setValue(dateChooserModel.getYear());
        selectedDateLabel.setText(dateChooserModel.getDateAsString());

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

        monthYearPanel = new javax.swing.JPanel();
        monthComboBox = new javax.swing.JComboBox();
        yearSpinner = new javax.swing.JSpinner();
        localeComboBox = new com.kybelksties.gui.LocaleComboBox();
        selectedDateLabel = new javax.swing.JTextField();
        monthDayScrollPane = new javax.swing.JScrollPane();
        monthDayTable = new javax.swing.JTable();

        setMaximumSize(new java.awt.Dimension(290, 180));
        setMinimumSize(new java.awt.Dimension(290, 180));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(290, 180));
        setLayout(new java.awt.BorderLayout());

        monthYearPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        monthComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                monthComboBoxActionPerformed(evt);
            }
        });
        monthYearPanel.add(monthComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 80, -1));

        yearSpinner.setModel(new javax.swing.SpinnerNumberModel(2016, 0, 2300, 1));
        yearSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                yearSpinnerStateChanged(evt);
            }
        });
        monthYearPanel.add(yearSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 20));

        localeComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                localeComboBoxActionPerformed(evt);
            }
        });
        monthYearPanel.add(localeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 90, -1));

        add(monthYearPanel, java.awt.BorderLayout.PAGE_START);

        selectedDateLabel.setEditable(false);
        selectedDateLabel.setText(org.openide.util.NbBundle.getMessage(DateChooser.class, "DateChooser.text")); // NOI18N
        selectedDateLabel.setMaximumSize(null);
        selectedDateLabel.setMinimumSize(null);
        selectedDateLabel.setName(""); // NOI18N
        selectedDateLabel.setPreferredSize(new java.awt.Dimension(46, 24));
        add(selectedDateLabel, java.awt.BorderLayout.PAGE_END);

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

        add(monthDayScrollPane, java.awt.BorderLayout.CENTER);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.kybelksties.gui.LocaleComboBox localeComboBox;
    private javax.swing.JComboBox monthComboBox;
    private javax.swing.JScrollPane monthDayScrollPane;
    private javax.swing.JTable monthDayTable;
    private javax.swing.JPanel monthYearPanel;
    private javax.swing.JTextField selectedDateLabel;
    private javax.swing.JSpinner yearSpinner;
    // End of variables declaration//GEN-END:variables
    private static final String CLASS_NAME = DateChooser.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
}
