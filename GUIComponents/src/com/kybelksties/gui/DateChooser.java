
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
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
    static private ArrayList<String> weekDaysLong;
    static private ArrayList<String> weekDaysShort;
    static private ArrayList<String> monthsLong;
    static private ArrayList<String> monthsShort;

    static
    {
        weekDaysLong = makeLocalNameArray(Calendar.DAY_OF_WEEK,Calendar.LONG);
        weekDaysShort = makeLocalNameArray(Calendar.DAY_OF_WEEK,Calendar.SHORT);
        monthsLong = makeLocalNameArray(Calendar.MONTH,Calendar.LONG);
        monthsShort = makeLocalNameArray(Calendar.MONTH,Calendar.SHORT);
    }

    private static ArrayList<String> makeLocalNameArray(int type, int style)
    {
        Locale locale = Locale.getDefault();
        ArrayList<String> reval = new ArrayList<>();
        Map<Integer, String> sorted = new TreeMap<>();
        Map<String, Integer> calendarMap =
                Calendar.getInstance().getDisplayNames(type,style,locale);
        for (String s : calendarMap.keySet())
        {
            sorted.put(calendarMap.get(s), s);
        }
        for (Integer i : sorted.keySet())
        {
            reval.add(sorted.get(i));
        }
        return reval;
    }
    
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

    private boolean showSample = true;

    /**
     * Get the value of showSample.
     *
     * @return the value of showSample
     */
    public boolean getShowSample()
    {
        return showSample;
    }

    /**
     * Set the value of showSample.
     *
     * @param showSample new value of showSample
     */
    public void setShowSample(boolean showSample)
    {
        this.showSample = showSample;
        updateComponents();
    }

    /**
     * Creates new form FontChooser.
     *
     * @param date initial font, if null use default
     */
    public DateChooser(Date date)
    {
        initComponents();

        selectedDateLabel.setSize(selectedDateLabel.getHeight(), 70);

        if (date != null)
        {
            theDate = date;
        }
        this.setSize(WIDTH, WIDTH);
        updateComponents();
    }

    /**
     * Default construct.
     */
    public DateChooser()
    {
        this(null);
    }

    /**
     * Update the GUI.
     */
    public final void updateComponents()
    {
        selectedDateLabel.setVisible(showSample);
        validate();
        repaint();
    }

    private void setDate()
    {
        selectedDateLabel.setText(theDate.toString());
        selectedDateLabel.invalidate();
    }

    /**
     * Show a dialog with this component.
     *
     * @param window parent
     * @param date   an initial font
     * @return the newly configured
     */
    static public Date showDialog(Window window, Date date)
    {
        final DateChooser chsr = new DateChooser(date);

        final JDialog dlg = new JDialog(window,
                                        Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setTitle("Font Chooser");
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

        jPanel1 = new javax.swing.JPanel();
        monthComboBox = new javax.swing.JComboBox();
        yearComboBox = new javax.swing.JComboBox();
        selectedDateLabel = new javax.swing.JTextField();
        monthDayScrollPane = new javax.swing.JScrollPane();
        monthDayTable = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(0, 0));
        setLayout(new java.awt.BorderLayout());

        monthComboBox.setModel(new DefaultComboBoxModel
            (monthsLong.toArray()));
        jPanel1.add(monthComboBox);

        yearComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(yearComboBox);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        selectedDateLabel.setEditable(false);
        selectedDateLabel.setText(org.openide.util.NbBundle.getMessage(DateChooser.class, "DateChooser.selectedDateLabel.text")); // NOI18N
        selectedDateLabel.setMaximumSize(new java.awt.Dimension(2147483647, 24));
        selectedDateLabel.setMinimumSize(new java.awt.Dimension(4, 24));
        selectedDateLabel.setPreferredSize(new java.awt.Dimension(46, 24));
        add(selectedDateLabel, java.awt.BorderLayout.PAGE_END);

        monthDayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        monthDayTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        monthDayTable.setAutoscrolls(false);
        monthDayTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        monthDayScrollPane.setViewportView(monthDayTable);

        add(monthDayScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox monthComboBox;
    private javax.swing.JScrollPane monthDayScrollPane;
    private javax.swing.JTable monthDayTable;
    private javax.swing.JTextField selectedDateLabel;
    private javax.swing.JComboBox yearComboBox;
    // End of variables declaration//GEN-END:variables
    private static final String CLASS_NAME = DateChooser.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
}
