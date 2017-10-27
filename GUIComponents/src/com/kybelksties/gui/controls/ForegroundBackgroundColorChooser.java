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

import com.kybelksties.gui.ColorUtils;
import com.kybelksties.gui.IndividualCellEditor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.openide.util.NbBundle;

/**
 *
 * @author kybelksd
 */
public class ForegroundBackgroundColorChooser extends javax.swing.JPanel
{

    private static final Class CLAZZ = ForegroundBackgroundColorChooser.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    ColorUtils.ContrastColorSet colorSet = new ColorUtils.ContrastColorSet();
    JPopupMenu colorTablePopup;
    transient IndividualCellEditor ice;

    private void deleteRows(int[] rows)
    {
        colorSet.remove(rows);
        updateComponents();
    }

    private void moveUp(int[] rows)
    {
        colorSet.up(rows);
        for (int row : rows)
        {
            colorCombinationTable.
                    setRowSelectionInterval(row > 1 ? row + 1 : 0,
                                            row > 1 ? row + 1 : 0);
            colorCombinationTable.setColumnSelectionInterval(0,
                                                             colorCombinationTable.
                                                             getColumnCount() -
                                                             1);
        }
        updateComponents();
    }

    private void moveDown(int[] rows)
    {
        colorSet.down(rows);
        for (int row : rows)
        {
            colorCombinationTable.
                    setRowSelectionInterval(row > 1 ? row + 1 : 0,
                                            row > 1 ? row + 1 : 0);
            colorCombinationTable.setColumnSelectionInterval(0,
                                                             colorCombinationTable.
                                                             getColumnCount() -
                                                             1);
        }
        updateComponents();
    }

    private void changeColor(int row)
    {

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
            ColorUtils.ContrastColorSet model =
                                        (ColorUtils.ContrastColorSet) table.
                                        getModel();
            Component c = super.getTableCellRendererComponent(table,
                                                              value,
                                                              isSelected,
                                                              hasFocus,
                                                              row,
                                                              column);
            c.setBackground(model.getColor());
            c.setForeground(model.getContrastColor(row));
            return c;
        }
    }

    /**
     * Get the value of backgroundColor.
     *
     * @return the value of backgroundColor
     */
    public Color getBackgroundColor()
    {
        return colorSet.getColor();
    }

    /**
     * Set the value of backgroundColor.
     *
     * @param color the color to set
     */
    public void setBackgroundColor(Color color)
    {
        colorSet.reset(color);
        updateComponents();
    }

    private Integer colorShift = 128;

    /**
     * Get the value of colorShift.
     *
     * @return the value of colorShift
     */
    public Integer getColorShift()
    {
        return colorShift;
    }

    /**
     * Set the value of colorShift.
     *
     * @param colorShift new value of colorShift
     */
    public final void setColorShift(Integer colorShift)
    {
        this.colorShift = colorShift;
        shiftSpinner.setValue(this.colorShift);
    }

    enum TableMenuActions
    {

        Delete, Up, Down, Change;

        @Override
        public String toString()
        {
            return this == Delete ?
                   NbBundle.getMessage(CLAZZ,
                                       "ForegroundBackgroundColorChooser.TableMenuActions.delete") :
                   this == Up ?
                   NbBundle.getMessage(CLAZZ,
                                       "ForegroundBackgroundColorChooser.TableMenuActions.up") :
                   this == Down ?
                   NbBundle.getMessage(CLAZZ,
                                       "ForegroundBackgroundColorChooser.TableMenuActions.down") :
                   this == Change ?
                   NbBundle.getMessage(CLAZZ,
                                       "ForegroundBackgroundColorChooser.TableMenuActions.change") :

                   NbBundle.getMessage(CLAZZ,
                                       "ForegroundBackgroundColorChooser.TableMenuActions.unknown");
        }

    }

    class TableMenu extends JPopupMenu
    {

        class AugmentedMenuItem extends JMenuItem
        {

            TableMenuActions action;

            AugmentedMenuItem(TableMenuActions action)
            {
                super(action.toString());
                this.action = action;
            }

            TableMenuActions getMenuAction()
            {
                return action;
            }
        }

        TableMenu()
        {
            ActionListener action = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    TableMenuActions command =
                                     ((AugmentedMenuItem) e.getSource()).
                                     getMenuAction();
                    int[] rows = colorCombinationTable.getSelectedRows();
                    switch (command)
                    {
                        case Delete:
                            ForegroundBackgroundColorChooser.this.deleteRows(
                                    rows);
                            break;
                        case Up:
                            ForegroundBackgroundColorChooser.this.moveUp(rows);
                            break;
                        case Down:
                            ForegroundBackgroundColorChooser.this.moveDown(rows);
                            break;
                        case Change:
                            ForegroundBackgroundColorChooser.this.changeColor(
                                    rows[0]);
                            break;
                    }
                }
            };

            for (TableMenuActions nma : TableMenuActions.values())
            {
                AugmentedMenuItem menuItem = new AugmentedMenuItem(nma);
                menuItem.addActionListener(action);
                add(menuItem);
            }
        }
    }

    /**
     * Creates new form ForegroundBackgroundColorChooser.
     */
    public ForegroundBackgroundColorChooser()
    {
        initComponents();

        colorCombinationTable.setRowSelectionAllowed(true);
        colorCombinationTable.setColumnSelectionInterval(
                0,
                colorCombinationTable.getColumnCount() - 1);
        colorTablePopup = new TableMenu();

        colorCombinationTable.setModel(colorSet);
        colorCombinationTable.setEnabled(true);
        ice = new IndividualCellEditor(colorCombinationTable,
                                       new MyTableCellRenderer());
        ice.insertComboEditor("TypeCombo",
                              ColorUtils.ContrastColorSet.Contrast.Type.values());
        ice.insertSpinnerEditor("ShiftSpinner", 128, 1, 255, 1);
        ice.setColumnEditor(0, "TypeCombo");
        ice.setColumnEditor(3, "ShiftSpinner");
        colorCombinationTable.setEnabled(true);

        setColorShift(colorShift);

        // Set the eventhandler for the color-chooser widget
        ColorSelectionModel model = colorChooser.getSelectionModel();
        ChangeListener changeListener = new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                updateComponents();
            }
        };
        model.addChangeListener(changeListener);

        JPanel j = new JPanel();
        j.setLayout(new BorderLayout());
        j.add(colorCombinationPane);
        colorChooser.setPreviewPanel(j);
        colorChooser.getPreviewPanel().setLayout(new BorderLayout());
        colorCombinationPane.setPreferredSize(new Dimension(600, 150));
        colorChooser.getPreviewPanel().add(colorCombinationPane);
        colorChooser.getPreviewPanel().setVisible(true);
        colorChooser.setColor(colorSet.getColor());

        updateComponents();
    }

    /**
     * Update the GUI.
     */
    public final void updateComponents()
    {
        colorSet.reset(colorChooser.getColor());
        colorCombinationTable.setModel(colorSet);
        validate();
        repaint();
    }

    /**
     * Shows the control embedded in a Dialog.
     *
     * @param window parent
     * @param color  the color we try to find complements for
     * @param colors
     * @return a set of contrast colors
     */
    static public ColorUtils.ContrastColorSet showDialog(Window window,
                                                         Color color,
                                                         Color... colors)
    {
        return showDialog(window, color, 128, false, colors);
    }

    /**
     * Show a dialog with this component.
     *
     * @param window                parent
     * @param color                 the color we try to find complements for
     * @param difference            shift value for shift complement
     * @param includeRGBcomplements if true then add complement to the contrasts
     * @param colors
     * @return a set of contrast colors
     */
    static public ColorUtils.ContrastColorSet showDialog(Window window,
                                                         Color color,
                                                         int difference,
                                                         boolean includeRGBcomplements,
                                                         Color... colors)
    {
        final ForegroundBackgroundColorChooser chsr =
                                               new ForegroundBackgroundColorChooser();
        chsr.colorSet = chsr.setProperties(color);
        if (includeRGBcomplements)
        {
            chsr.colorSet.addExactColorContrast(Color.RED);
            chsr.colorSet.addExactColorContrast(Color.GREEN);
            chsr.colorSet.addExactColorContrast(Color.BLUE);
        }
        if (colors != null)
        {
            for (Color c : colors)
            {
                chsr.colorSet.addExactColorContrast(c);
            }
        }

        chsr.shiftSpinner.setValue(difference);

        final JDialog dlg = new JDialog(window,
                                        Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setTitle(NbBundle.getMessage(
                CLAZZ,
                "ForegroundBackgroundColorChooser.title"));
        dlg.getContentPane().setLayout(new AbsoluteLayout());
        Dimension chsrDim = chsr.getPreferredSize();
        dlg.getContentPane().setSize(chsrDim);
        AbsoluteConstraints constraints =
                            new AbsoluteConstraints(2,
                                                    2,
                                                    chsrDim.width,
                                                    chsrDim.height);
        dlg.add(chsr, constraints);
        JButton okButton = new JButton(
                NbBundle.getMessage(CLAZZ,
                                    "ForegroundBackgroundColorChooser.ok"));
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

        return chsr.setProperties(chsr.getBackgroundColor());

    }

    /**
     * Set a new color, calculate new contrasts and update the GUI.
     *
     * @param color the color we try to find complements for
     * @return a set of contrast colors
     */
    public ColorUtils.ContrastColorSet setProperties(Color color)
    {
        if (color == null)
        {
            color = getBackgroundColor();
        }
        colorSet.reset(color);
        updateComponents();

        return colorSet;
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

        colorCombinationPane = new javax.swing.JScrollPane();
        colorCombinationTable = new javax.swing.JTable();
        shiftSpinner = new javax.swing.JSpinner();
        suitableForegroundButton = new javax.swing.JButton();
        colorChooser = new javax.swing.JColorChooser();
        exactForegroundButton = new javax.swing.JButton();
        greyForegroundButton = new javax.swing.JButton();
        shiftButton = new javax.swing.JButton();
        hintButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(630, 350));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        colorCombinationTable.setModel(new javax.swing.table.DefaultTableModel(
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
        colorCombinationTable.setToolTipText(org.openide.util.NbBundle.getMessage(ForegroundBackgroundColorChooser.class, "ForegroundBackgroundColorChooser.colorCombinationTable.toolTipText")); // NOI18N
        colorCombinationTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                colorCombinationTableMouseClicked(evt);
            }
        });
        colorCombinationPane.setViewportView(colorCombinationTable);

        add(colorCombinationPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 510, 100));

        shiftSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 250, 10));
        shiftSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                shiftSpinnerStateChanged(evt);
            }
        });
        add(shiftSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 410, 60, -1));

        suitableForegroundButton.setText(org.openide.util.NbBundle.getMessage(ForegroundBackgroundColorChooser.class, "ForegroundBackgroundColorChooser.suitableForegroundButton.text")); // NOI18N
        suitableForegroundButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                suitableForegroundButtonActionPerformed(evt);
            }
        });
        add(suitableForegroundButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 410, 210, -1));

        colorChooser.setMaximumSize(new java.awt.Dimension(612, 305));
        add(colorChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 330));

        exactForegroundButton.setText(org.openide.util.NbBundle.getMessage(ForegroundBackgroundColorChooser.class, "ForegroundBackgroundColorChooser.exactForegroundButton.text")); // NOI18N
        exactForegroundButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exactForegroundButtonActionPerformed(evt);
            }
        });
        add(exactForegroundButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 380, 190, -1));

        greyForegroundButton.setText(org.openide.util.NbBundle.getMessage(ForegroundBackgroundColorChooser.class, "ForegroundBackgroundColorChooser.greyForegroundButton.text")); // NOI18N
        greyForegroundButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                greyForegroundButtonActionPerformed(evt);
            }
        });
        add(greyForegroundButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 380, 220, -1));

        shiftButton.setText(org.openide.util.NbBundle.getMessage(ForegroundBackgroundColorChooser.class, "ForegroundBackgroundColorChooser.shiftButton.text")); // NOI18N
        shiftButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                shiftButtonActionPerformed(evt);
            }
        });
        add(shiftButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 410, 200, -1));

        hintButton.setText(org.openide.util.NbBundle.getMessage(ForegroundBackgroundColorChooser.class, "ForegroundBackgroundColorChooser.hintButton.text")); // NOI18N
        hintButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                hintButtonActionPerformed(evt);
            }
        });
        add(hintButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 380, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void shiftSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_shiftSpinnerStateChanged
    {//GEN-HEADEREND:event_shiftSpinnerStateChanged
        updateComponents();
    }//GEN-LAST:event_shiftSpinnerStateChanged

    private void suitableForegroundButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_suitableForegroundButtonActionPerformed
    {//GEN-HEADEREND:event_suitableForegroundButtonActionPerformed
        if (!colorSet.addComplement())
        {
            String message = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddComplement");
            String header = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddHeader");
            JOptionPane.showMessageDialog(colorChooser,
                                          message,
                                          header,
                                          JOptionPane.WARNING_MESSAGE);
        }
        updateComponents();
    }//GEN-LAST:event_suitableForegroundButtonActionPerformed

    private void exactForegroundButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exactForegroundButtonActionPerformed
    {//GEN-HEADEREND:event_exactForegroundButtonActionPerformed
        Color color = JColorChooser.showDialog(
              exactForegroundButton,
              NbBundle.getMessage(
                      CLAZZ,
                      "ForegroundBackgroundColorChooser.foreground"),
              Color.BLACK);
        if (!colorSet.addExactColorContrast(color))
        {
            String message = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddForeground",
                   ColorUtils.xtermColorString(color, true, true));
            String header = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddHeader");
            JOptionPane.showMessageDialog(colorChooser,
                                          message,
                                          header,
                                          JOptionPane.WARNING_MESSAGE);
        }
        updateComponents();
    }//GEN-LAST:event_exactForegroundButtonActionPerformed

    private void greyForegroundButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_greyForegroundButtonActionPerformed
    {//GEN-HEADEREND:event_greyForegroundButtonActionPerformed
        if (!colorSet.addGreyContrast())
        {
            String message = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddGrey");
            String header = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddHeader");
            JOptionPane.showMessageDialog(colorChooser,
                                          message,
                                          header,
                                          JOptionPane.WARNING_MESSAGE);
        }
        updateComponents();
    }//GEN-LAST:event_greyForegroundButtonActionPerformed

    private void shiftButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shiftButtonActionPerformed
    {//GEN-HEADEREND:event_shiftButtonActionPerformed
        if (!colorSet.addShiftContrast((int) shiftSpinner.getValue()))
        {
            String message = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddShift",
                   (int) shiftSpinner.getValue());
            String header = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddHeader");
            JOptionPane.showMessageDialog(colorChooser,
                                          message,
                                          header,
                                          JOptionPane.WARNING_MESSAGE);
        }
        updateComponents();

    }//GEN-LAST:event_shiftButtonActionPerformed

    private void hintButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_hintButtonActionPerformed
    {//GEN-HEADEREND:event_hintButtonActionPerformed
        Color color = JColorChooser.showDialog(
              hintButton,
              NbBundle.getMessage(CLAZZ,
                                  "ForegroundBackgroundColorChooser.foreground"),
              Color.BLACK);
        if (!colorSet.addHintContrast(color))
        {
            String message = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddHinted",
                   ColorUtils.xtermColorString(color, true, true));
            String header = NbBundle.getMessage(
                   CLAZZ,
                   "ForegroundBackgroundColorChooser.cannotAddHeader");
            JOptionPane.showMessageDialog(colorChooser,
                                          message,
                                          header,
                                          JOptionPane.WARNING_MESSAGE);
        }
        updateComponents();

    }//GEN-LAST:event_hintButtonActionPerformed

    private void colorCombinationTableMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_colorCombinationTableMouseClicked
    {//GEN-HEADEREND:event_colorCombinationTableMouseClicked
        colorCombinationTable.setComponentPopupMenu(colorTablePopup);
    }//GEN-LAST:event_colorCombinationTableMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JColorChooser colorChooser;
    private javax.swing.JScrollPane colorCombinationPane;
    private javax.swing.JTable colorCombinationTable;
    private javax.swing.JButton exactForegroundButton;
    private javax.swing.JButton greyForegroundButton;
    private javax.swing.JButton hintButton;
    private javax.swing.JButton shiftButton;
    private javax.swing.JSpinner shiftSpinner;
    private javax.swing.JButton suitableForegroundButton;
    // End of variables declaration//GEN-END:variables
}
