
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
import javax.swing.table.TableColumnModel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * Custom component to choose a font.
 *
 * @author kybelksd
 */
public class DateChooser extends javax.swing.JPanel
{

    private Date theDate = null;
    static private CalendarModel calendarModel = new CalendarModel();
    static private Locale locale = Locale.getDefault();

    /**
     * Get the value of configuredFont
     *
     * @return the value of configuredFont
     */
    public Date getDate()
    {
        return theDate;
    }

    /**
     * Set the value of configuredFont.
     *
     * @param newDate new value of configuredFont
     */
    public void setDate(Date newDate)
    {
        if (newDate == null ||
            !newDate.equals(theDate))
        {
            propertyChangeSupport.firePropertyChange(
                    "configuredFont",
                    theDate,
                    newDate);

        }

        this.theDate = newDate;
    }

    private transient final PropertyChangeSupport propertyChangeSupport =
                                                  new PropertyChangeSupport(
                                                          this);

    public void setLocale(Locale locale)
    {
        this.locale = locale;
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
    public boolean getShowSample()
    {
        return showDateLabel;
    }

    /**
     * Set the value of showSample.
     *
     * @param showSample new value of showSample
     */
    public void setShowSample(boolean showSample)
    {
        this.showDateLabel = showSample;
        updateComponents(locale, theDate);
    }

    /**
     * Creates new form FontChooser.
     *
     * @param date   initial font, if null use default
     * @param locale
     */
    public DateChooser(Date date, Locale locale)
    {
        initComponents();
        setColumnWidths();
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        monthDayTable.setRowSelectionAllowed(false);
        selectedDateLabel.setSize(selectedDateLabel.getHeight(), 70);

        if (date != null)
        {
            theDate = date;
        }
        this.setSize(WIDTH, WIDTH);
        updateComponents(locale, date);
    }

    /**
     * Default construct.
     */
    public DateChooser()
    {
        this(null, Locale.getDefault());
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
     *
     * @param locale
     * @param date
     */
    public final void updateComponents(Locale locale, Date date)
    {
        selectedDateLabel.setVisible(showDateLabel);
        if (locale == null)
        {
            locale = Locale.getDefault();
        }
        theDate = date != null ? date : Calendar.getInstance(locale).getTime();
        calendarModel = new CalendarModel(locale, date);
        monthDayTable.setModel(calendarModel);
        monthComboBox.setSelectedItem(calendarModel.getMonth());
        validate();
        repaint();
    }

    public void setDate()
    {
        selectedDateLabel.setText(theDate.toString());
        selectedDateLabel.invalidate();
    }

    /**
     * Show a dialog with this component.
     *
     * @param window parent
     * @param date   an initial font
     * @param locale the locale properties
     * @return the newly configured
     */
    static public Date showDialog(Window window, final Date date, Locale locale)
    {
        final DateChooser chsr = new DateChooser(date, locale);

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
                chsr.updateComponents(chsr.getLocale(), date);
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
        selectedDateLabel = new javax.swing.JTextField();
        monthDayScrollPane = new javax.swing.JScrollPane();
        monthDayTable = new javax.swing.JTable();

        setMaximumSize(new java.awt.Dimension(290, 180));
        setMinimumSize(new java.awt.Dimension(290, 180));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(290, 180));
        setLayout(new java.awt.BorderLayout());

        monthComboBox.setModel(new DefaultComboBoxModel
            (calendarModel.getMonthsLong().toArray()));
        monthComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                monthComboBoxActionPerformed(evt);
            }
        });
        monthYearPanel.add(monthComboBox);

        yearSpinner.setModel(new javax.swing.SpinnerNumberModel(2016, 0, 2300, 1));
        monthYearPanel.add(yearSpinner);

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

        monthDayTable.setModel(calendarModel);
        monthDayTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        monthDayTable.setAutoscrolls(false);
        monthDayTable.setColumnSelectionAllowed(false);
        monthDayTable.setMaximumSize(new java.awt.Dimension(450, 240));
        monthDayTable.setMinimumSize(new java.awt.Dimension(450, 240));
        monthDayTable.setPreferredSize(new java.awt.Dimension(450, 240));
        monthDayTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        monthDayTable.setShowHorizontalLines(false);
        monthDayTable.setShowVerticalLines(false);
        monthDayTable.getTableHeader().setResizingAllowed(false);
        monthDayTable.getTableHeader().setReorderingAllowed(false);
        monthDayScrollPane.setViewportView(monthDayTable);

        add(monthDayScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void monthComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_monthComboBoxActionPerformed
    {//GEN-HEADEREND:event_monthComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_monthComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
