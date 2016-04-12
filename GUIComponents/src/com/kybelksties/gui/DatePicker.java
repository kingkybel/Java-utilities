
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * Custom component to choose a date.
 *
 * @author kybelksd
 */
public class DatePicker extends javax.swing.JPanel
{

    private DateChooserModel dateChooserModel;
    private final DateChooser dateChooser;

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

    private boolean showTable = false;

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

    // Define Show Popup ActionListener
    static class ShowPopupActionListener implements ActionListener
    {

        private final Component parent;
        private final Component popupComp;

        ShowPopupActionListener(Component parent, Component popupComp)
        {
            this.parent = parent;
            this.popupComp = popupComp;
        }

        @Override
        public synchronized void actionPerformed(ActionEvent actionEvent)
        {
            PopupFactory factory = PopupFactory.getSharedInstance();
            Random random = new Random();
            int x = random.nextInt(200);
            int y = random.nextInt(200);
            final Popup popup = factory.getPopup(parent,
                                                 popupComp,
                                                 parent.getLocationOnScreen().x,
                                                 parent.getLocationOnScreen().y);
            popup.show();
            ActionListener hider = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    popup.hide();
                }
            };
        }
    }

    /**
     * Creates new form DateChooser.
     *
     * @param locale local names and formats etc
     * @param date   a date for which to display a calendar month
     */
    public DatePicker(Locale locale, Date date)
    {
        initComponents();
        dateChooserModel = new DateChooserModel(locale, date);
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
        yearSpinner.setModel(dateChooserModel);
        monthComboBox.setModel(dateChooserModel);
        monthComboBox.setModel(new DefaultComboBoxModel(
                dateChooserModel.getMonthsLong().toArray()));
        dateChooser = new DateChooser(locale, date);
        ActionListener actionListener = new ShowPopupActionListener(
                       this,
                       dateChooser);
        showTableButton.addActionListener(actionListener);
        updateComponents();
    }

    /**
     * Default construct.
     */
    public DatePicker()
    {
        this(Locale.getDefault(), null);
    }

    /**
     * Update the GUI.
     */
    public final void updateComponents()
    {

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

        monthComboBox.setSelectedItem(dateChooserModel.getMonth());
        yearSpinner.setValue(dateChooserModel.getYear());

        daySpinner.setModel(
                new SpinnerNumberModel(dateChooserModel.getDayOfMonth(),
                                       1,
                                       dateChooserModel.getMaxDayOfMonth(),
                                       1));

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
        final DatePicker chsr = new DatePicker(locale, date);

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
        daySpinner = new javax.swing.JSpinner();
        monthComboBox = new javax.swing.JComboBox();
        yearSpinner = new javax.swing.JSpinner();
        showTableButton = new javax.swing.JCheckBox();

        setName(""); // NOI18N
        setLayout(new java.awt.CardLayout());

        monthYearPanel.setLayout(new javax.swing.BoxLayout(monthYearPanel, javax.swing.BoxLayout.LINE_AXIS));

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
        monthYearPanel.add(daySpinner);

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
        monthYearPanel.add(monthComboBox);

        yearSpinner.setModel(new javax.swing.SpinnerNumberModel(2016, 0, 2300, 1));
        yearSpinner.setMaximumSize(new java.awt.Dimension(75, 20));
        yearSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                yearSpinnerStateChanged(evt);
            }
        });
        monthYearPanel.add(yearSpinner);

        showTableButton.setText(org.openide.util.NbBundle.getMessage(DatePicker.class, "DatePicker.showTableButton.text")); // NOI18N
        showTableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/down.png"))); // NOI18N
        showTableButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showTableButtonActionPerformed(evt);
            }
        });
        monthYearPanel.add(showTableButton);

        add(monthYearPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void monthComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_monthComboBoxActionPerformed
    {//GEN-HEADEREND:event_monthComboBoxActionPerformed
        dateChooserModel.setMonthByIndex(monthComboBox.getSelectedIndex());
        updateComponents();
    }//GEN-LAST:event_monthComboBoxActionPerformed

    private void yearSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_yearSpinnerStateChanged
    {//GEN-HEADEREND:event_yearSpinnerStateChanged
        updateComponents();
    }//GEN-LAST:event_yearSpinnerStateChanged

    private void daySpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_daySpinnerStateChanged
    {//GEN-HEADEREND:event_daySpinnerStateChanged
        dateChooserModel.setDayOfMonth((int) daySpinner.getValue());
        updateComponents();
    }//GEN-LAST:event_daySpinnerStateChanged

    private void showTableButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showTableButtonActionPerformed
    {//GEN-HEADEREND:event_showTableButtonActionPerformed
        showTable = showTableButton.isSelected();
        if (showTable)
        {
            showTableButton.setIcon(
                    new ImageIcon(getClass().getResource("/images/up.png")));
        }
        else
        {
            showTableButton.setIcon(
                    new ImageIcon(getClass().getResource("/images/down.png")));
        }

        updateComponents();
    }//GEN-LAST:event_showTableButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner daySpinner;
    private javax.swing.JComboBox monthComboBox;
    private javax.swing.JPanel monthYearPanel;
    private javax.swing.JCheckBox showTableButton;
    private javax.swing.JSpinner yearSpinner;
    // End of variables declaration//GEN-END:variables
    private static final String CLASS_NAME = DatePicker.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
}
